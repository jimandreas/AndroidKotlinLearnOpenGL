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
import android.opengl.GLSurfaceView
import com.androidkotlin.opengl.realtime.RendererBaseClass
import com.androidkotlin.opengl.ui.ViewModel
import com.androidkotlin.opengl.util.*
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3
import timber.log.Timber
import java.lang.Math.random
import java.nio.ByteBuffer
import java.nio.ByteOrder
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
    private val planet = ObjFile(context)
    private val rock = ObjFile(context)
    private var planetDiffuseTextureMap = 0
    private var rockVBO = IntArray(1)
    private var numberOfRocks = 0

    //    private val camera = Camera(Vector3(0.0, 0.0, 155.0))
    private val camera = Camera(Vector3(0.0, 0.0, 10.0))
//    private var vbo = IntArray(1)

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

        // load textures
        // -----------------------------------------------------------------------------
        planetDiffuseTextureMap = loadTextureFromAsset163(context, "planet_Quom1200.png")
//        planetDiffuseTextureMap = loadTextureFromAsset163(context,"container2.png")

        planetShader.use()
//        planetShader.setInt("texture_diffuse1", planetDiffuseTextureMap)
        planetShader.setInt("texture_diffuse1", 0)

        /*
         * generate a large list of semi-random model transformation matrices
         * ------------------------------------------------------------------
         */
        numberOfRocks = 1000
        val radius = 150.0
        val offset = 25.0
        val modelMatrices = mutableListOf<Matrix4>()
        for (i in 0..numberOfRocks) {
            var model = Matrix4()
            val angle = i.toDouble() / numberOfRocks.toDouble() * 360.0
            var displacement = ((random() - 0.5) * 2 * offset * 100) / 100.0 - offset
            val x = sin(angle) * radius + displacement
//            displacement = (random() % (2 * offset * 100).toInt()) / 100.0 - offset
            displacement = ((random() - 0.5) * 2 * offset * 100) / 100.0 - offset
            val y = displacement * 0.4 // keep height of asteroid field smaller compared to x and z
//            displacement = (random() % (2 * offset * 100).toInt()) / 100.0 - offset
            displacement = ((random() - 0.5) * 2 * offset * 100) / 100.0 - offset
            val z = cos(angle) * radius + displacement
            model = model.translate(Vector3(x, y, z))

            // 2. scale: Scale between 0.05 and 0.25
            val scale = (random() % 20) / 100.0 + 0.05
            model = model.scale(Vector3(scale))

            // 3. rotation : add random rotation around a (semi) randomly
            //    picked rotation axis vector
            val rotAngle = random() % 360
            model = model.rotate(Vector3(0.4, 0.6, 0.8), rotAngle)

            // 4. now add to list of matrices
            modelMatrices.add(model)
        }

        // configure instanced array
        // -------------------------

        val nativeFloatBuffer = ByteBuffer
                .allocateDirect(modelMatrices.size * 16 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        val convertedFloatArray = matrix4toFloatArray(modelMatrices)
        nativeFloatBuffer!!.put(convertedFloatArray).position(0)
        val buffer = IntArray(1)
        glGenBuffers(1, buffer, 0)
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, buffer[0])
        GLES30.glBufferData(GL_ARRAY_BUFFER, numberOfRocks * 16 * 4, nativeFloatBuffer, GL_STATIC_DRAW)
        checkGLerr("OSC1")

        planet.parse("planet")
        planet.build_buffers()

        rock.parse("rock")
        rock.build_buffers()

        // now hack in the assignment of 4 VEC4 slots to match the Matrix4
        // array entries in the shader
        //   ref:   layout (location = 3) in mat4 aInstanceMatrix;

        rockVBO = rock.getVBO()
        GLES30.glBindVertexArray(rockVBO[0])
        GLES30.glEnableVertexAttribArray(3)
        GLES30.glVertexAttribPointer(3, 4, GLES30.GL_FLOAT, false, 16 * 4, 0)
        GLES30.glEnableVertexAttribArray(4)
        GLES30.glVertexAttribPointer(4, 4, GLES30.GL_FLOAT, false, 16 * 4, 4 * 4)
        GLES30.glEnableVertexAttribArray(5)
        GLES30.glVertexAttribPointer(5, 4, GLES30.GL_FLOAT, false, 16 * 4, 8 * 4)
        GLES30.glEnableVertexAttribArray(6)
        GLES30.glVertexAttribPointer(6, 4, GLES30.GL_FLOAT, false, 16 * 4, 12 * 4)
        GLES30.glBindVertexArray(0)
    }

    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")

        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        checkGLerr("ODF1")

        // view/projection transformations
        var projection = Matrix4()
        projection = projection.setToPerspective(
                0.1,
                100.0,
                camera.zoom,
                screenWidth * 1.0 / screenHeight * 1.0)

        camera.setRotation(deltaX.toDouble(), deltaY.toDouble())
        deltaX = 0.0f
        deltaY = 0.0f
        val view = camera.getViewMatrix()
//        asteroidShader.use()
//        asteroidShader.setMat4("projection", toFloatArray16(projection))
//        asteroidShader.setMat4("view", toFloatArray16(view))

        planetShader.use()
        planetShader.setMat4("projection", toFloatArray16(projection))
        planetShader.setMat4("view", toFloatArray16(view))

        val model = Matrix4() // identity matrix
        //model = model.translate(Vector3(0.0, -3.0, 0.0))
        //model = model.scale(Vector3(4.0, 4.0, 4.0))
        planetShader.setMat4("model", toFloatArray16(model))

        // bind diffuse map
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, planetDiffuseTextureMap)

        planet.render(planetShader)

        // Draw meteorites (rocks)
        asteroidShader.use()
        asteroidShader.setInt("texture_diffuse1", 0)
        glActiveTexture(GL_TEXTURE0)
        GLES30.glBindVertexArray(rockVBO[0])
        GLES30.glDrawElementsInstanced(GL_TRIANGLES,
                rock.getVertexCount(),
                GL_UNSIGNED_INT,
                0, numberOfRocks)
        GLES30.glBindVertexArray(0)



    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        Timber.i("OnSurfaceChanged, width %d, height %d", width, height)
        // Set the OpenGL viewport to the same size as the surface.
        glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height

        lastX = screenWidth / 2.0f
        lastY = screenHeight / 2.0f

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio * scaleCurrentF
        val right = ratio * scaleCurrentF
        val bottom = -1.0f * scaleCurrentF
        val top = 1.0f * scaleCurrentF
        val near = 1.0f
        val far = 20.0f
    }

    companion object {
        private val vertexShaderSource = """
            #version 300 es
            precision mediump float;
            layout (location = 0) in vec3 aPos;
            void main()
            {
                gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
            }
        """.trimIndent()

        private val fragmentShaderSource = """
            #version 300 es
            precision mediump float;
            out vec4 FragColor;
            void main()
            {
                FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
            }
        """.trimIndent()


        private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)

        private var screenWidth = 0
        private var screenHeight = 0

        /*
        * Store the accumulated touch based manipulation
        */
        private val accumulatedRotation = FloatArray(16)
        private val accumulatedTranslation = FloatArray(16)
        private val accumulatedScaling = FloatArray(16)

        /*
         * Store the current rotation.
         */
        private val incrementalRotation = FloatArray(16)

    }

    private var lastX = 0f
    private var lastY = 0f
}
