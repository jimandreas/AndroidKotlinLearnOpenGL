/*
 * All code samples, unless explicitly stated otherwise,
 * are licensed under the terms of the CC BY-NC 4.0 license
 * as published by Creative Commons, either version 4 of the License,
 * or (at your option) any later version. You can find a human-readable
 * format of the license here:
 * https://creativecommons.org/licenses/by-nc/4.0/
 * and the full license here:
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Translation to kotlin and adaptation to android architecture:
 * Jim Andreas  jim@jimandreas.com
 */

package com.androidkotlin.opengl.learnopengl

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLES30
import android.opengl.GLES30.glVertexAttribDivisor
import android.opengl.GLSurfaceView
import com.androidkotlin.bigfile.util.Camera
import com.androidkotlin.opengl.realtime.RendererBaseClass
import com.androidkotlin.opengl.ui.ViewModel
import com.androidkotlin.opengl.util.*
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3
import timber.log.Timber
import java.lang.Math.random
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

// ref: see:
// https://learnopengl.com/code_viewer_gh.php?code=src/4.advanced_opengl/10.3.asteroids_instanced/asteroids_instanced.cpp

class Renderer4103AdvancedAsteroidsInstanced(
        val context: Context,
        viewModel: ViewModel
) : RendererBaseClass(context, viewModel), GLSurfaceView.Renderer {

    private val asteroidShader = Shader()
    private val planetShader = Shader()
    private val planetVBO = ObjFile(context)
    private val asteroidVBO = ObjFile(context)
    private var planetTextureMap = 0
    private var asteroidTextureMap = 0
    private var asteroidObjVBO = IntArray(1)

    private var rockMatrix4buffer = IntArray(1)
    private var rockVertexArray = IntArray(1)
    private lateinit var modelMatrices: MutableList<Matrix4>
    private lateinit var nativeFloatBuffer: FloatBuffer
    private var numberOfRocks = 1000
    /*
     * android notes (sony Xperia)
     *     50K - too big
     *     25K - OK
     *     37K - curious system failure
     *     30K - OK, also fine on emulator with HW assist
     *      1K - looks cooler
     */
    private val camera = Camera(Vector3(0.0, 0.0, 20.0))

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {

        // Enable depth testing
        glEnable(GL_DEPTH_TEST)

        asteroidShader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "10.3.asteroids.vs",
                "10.3.asteroids.fs")

        planetShader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "10.3.planet.vs",
                "10.3.planet.fs")


        /*
         * load textures
         */
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        planetTextureMap = loadTextureFromAsset163(context, "planet_Quom1200.png")
        planetShader.use()
        planetShader.setInt("texture_diffuse1", 1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, planetTextureMap)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        asteroidTextureMap = loadTextureFromAsset163(context, "rock.png")
        asteroidShader.use()
        asteroidShader.setInt("texture_diffuse1", 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, asteroidTextureMap)


        planetVBO.parse("planet")
        planetVBO.build_buffers()

        asteroidVBO.parse("rock")
        asteroidVBO.build_buffers()
        asteroidObjVBO = asteroidVBO.getVBO()

        /*
         * generate a large list of semi-random model transformation matrices
         * ------------------------------------------------------------------
         */

        val radius = 10.0
        val offset = 2.5
        modelMatrices = MutableList(numberOfRocks) {
            var model = Matrix4()
            val angle = it.toDouble() / numberOfRocks.toDouble() * 360.0
            var displacement = ((random() - 0.5) * 2 * offset * 100) / 100.0 - offset
            val x = sin(angle) * radius + displacement
            displacement = (random() % (2 * offset * 100).toInt()) / 100.0 - offset
            val y = displacement * 0.4 + 1.75// keep height of asteroid field smaller compared to x and z
            displacement = (random() * (2 * offset * 100).toInt()) / 100.0 - offset
            val z = cos(angle) * radius + displacement
            model = model.translate(Vector3(x, y, z))
            //Timber.i("translate %6.2f %6.2f %6.2f", x, y, z)

            // 2. scale: Scale between 0.05 and 0.25
//            val scale = (random() % 20) / 100.0 + 0.05
            val scale = (random() * 100.0) / 1000.0 + 0.01
            model = model.scale(Vector3(scale))

            // 3. rotation : add random rotation around a (semi) randomly
            //    picked rotation axis vector
            val rotAngle = random() * 360.0
            model = model.rotate(Vector3(0.4, 0.6, 0.8), rotAngle)

            // 4. now add to list of matrices
            model
        }

        // configure instanced array
        // -------------------------

        nativeFloatBuffer = matrix4ArraytoFloatBuffer(modelMatrices)

        glGenBuffers(1, rockMatrix4buffer, 0)
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, rockMatrix4buffer[0])
        GLES30.glBufferData(GL_ARRAY_BUFFER, numberOfRocks * 16 * 4, nativeFloatBuffer, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        checkGLerr("OSC1")

        // now hack in the assignment of 4 VEC4 slots to match the Matrix4
        // array entries in the shader
        //   ref:   layout (location = 3) in mat4 aInstanceMatrix;

        GLES30.glGenVertexArrays(1, rockVertexArray, 0)
        GLES30.glBindVertexArray(rockVertexArray[0])

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, asteroidObjVBO[0])
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, rockMatrix4buffer[0])

        GLES30.glEnableVertexAttribArray(3)
        GLES30.glEnableVertexAttribArray(4)
        GLES30.glEnableVertexAttribArray(5)
        GLES30.glEnableVertexAttribArray(6)

        GLES30.glVertexAttribPointer(3, 4, GLES30.GL_FLOAT, false, 16 * 4, 0)
        GLES30.glVertexAttribPointer(4, 4, GLES30.GL_FLOAT, false, 16 * 4, 4 * 4)
        GLES30.glVertexAttribPointer(5, 4, GLES30.GL_FLOAT, false, 16 * 4, 8 * 4)
        GLES30.glVertexAttribPointer(6, 4, GLES30.GL_FLOAT, false, 16 * 4, 12 * 4)

        glVertexAttribDivisor(3, 1)
        glVertexAttribDivisor(4, 1)
        glVertexAttribDivisor(5, 1)
        glVertexAttribDivisor(6, 1)
        //GLES30.glBindVertexArray(0)
        checkGLerr("OSC2")
    }

    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")

        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        checkGLerr("ODF1")

        // view/projection transformations
        var projectionM4 = Matrix4()
        val zoom = camera.zoomInOut(deltaZoom)
        projectionM4 = projectionM4.setToPerspective(
                0.1,
                100.0,
                zoom,  //45.0,
                screenWidth.toDouble() / screenHeight.toDouble())
        camera.setRotation(deltaX, deltaY)
        camera.moveRight(deltaX)
        camera.moveForward(deltaY)
        deltaX = 0.0f
        deltaY = 0.0f
        deltaZoom = 0.0f

        val view = camera.getViewMatrix()

        /*
         Draw asteroids
         */
        asteroidShader.use()
        asteroidShader.setMat4("projection", toFloatArray16(projectionM4))
        asteroidShader.setMat4("view", toFloatArray16(view))


        /*
         * 1: pull data directly from modelMatrices and pass
         *    to the model variable in the shader
         *    --> the "aInstanceMatrix" instance variable is removed
         *    in the shader in this case
         */
//        val iterator = modelMatrices.iterator()
//        while (iterator.hasNext()) {
//            val modela = iterator.next()
//            asteroidShader.setMat4("model", toFloatArray16(modela))
//            asteroidVBO.render()
//        }

        /*
         * 1b: attempt to pull data from NIO float buffer (check the data!)
         *    --> the "aInstanceMatrix" instance variable is removed
         *    in the shader in this case too.
         */
//        if (!testOfNativeBufferDone) {
//            nativeFloatBuffer.get(floatArray, 0, numberOfRocks * 16)
//            testOfNativeBufferDone = true
//        }
//        for (i in 0 until numberOfRocks) {
//            val floatSubArray = FloatArray(16)
//            for (j in 0 until 16) {
//                floatSubArray[j] = floatArray[i*16 + j]
//            }
//            asteroidShader.setMat4("model", floatSubArray)
//            asteroidVBO.render()
//        }


        /*
         * 2: draw instanced asteroids
         *    Now it works!!
         *    Do NOT rebind the rockMatrix4 buffer :-) - it is bound in the rockVertexArray already.
         *    See also:  https://stackoverflow.com/a/21652955
         *      for a good "versioned" explanation of VertexArrays vs Buffers
         */
        GLES30.glBindVertexArray(rockVertexArray[0])
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, asteroidObjVBO[0])
        GLES30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)
        // texture coordinate attribute
        GLES30.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4)
        GLES30.glEnableVertexAttribArray(2)
//        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, rockMatrix4buffer[0])
        val vertexCount = asteroidVBO.getVertexCount()
        GLES30.glDrawArraysInstanced(GL_TRIANGLES,
                0,
                vertexCount,
                numberOfRocks)

        checkGLerr("ODF2")

        /*
         * draw planetVBO
         */

        planetShader.use()
        planetShader.setMat4("projection", toFloatArray16(projectionM4))
        planetShader.setMat4("view", toFloatArray16(view))
        val modelp = Matrix4() // identity matrix
        planetShader.setMat4("model", toFloatArray16(modelp))
        planetVBO.render()

        checkGLerr("ODF3")
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        Timber.i("OnSurfaceChanged, width %d, height %d", width, height)
        // Set the OpenGL viewport to the same size as the surface.
        glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height

        lastX = screenWidth / 2.0f
        lastY = screenHeight / 2.0f
    }

    companion object {
        private var screenWidth = 0
        private var screenHeight = 0
    }
    private var lastX = 0f
    private var lastY = 0f
}
