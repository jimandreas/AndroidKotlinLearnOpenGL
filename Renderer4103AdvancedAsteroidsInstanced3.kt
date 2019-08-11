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
import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glGenVertexArrays
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
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

// ref: see:
// https://learnopengl.com/code_viewer_gh.php?code=src/4.advanced_opengl/10.3.asteroids_instanced/asteroids_instanced.cpp

class Renderer4103AdvancedAsteroidsInstanced3(
        val context: Context,
        viewModel: ViewModel
) : RendererBaseClass(context, viewModel), GLSurfaceView.Renderer {

    private val asteroidShader = Shader()
    private val planetShader = Shader()

    private val camera = Camera(Vector3(0.0, 0.0, -10.0))

//    private var vbo = IntArray(1)
//    private var vao = IntArray(1)
//    //private var ebo = IntArray(1)

    private val vertexBuffer: FloatBuffer
    private var texture1 = 0
    private var texture2 = 0

    private val vertices = floatArrayOf(
            0.5f,  0.5f, 0.0f,   1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f,   1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // bottom left
            -0.5f,  0.5f, 0.0f,   0.0f, 1.0f  // top left
    )

    val indices = intArrayOf(
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    )

    init {
        // initialize vertex byte buffer for shape coordinates
        val bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertices.size * 4)
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder())
        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer()
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices)
        // set the buffer to read the first coordinate
        vertexBuffer.position(0)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {

        // Enable depth testing
        glEnable(GL_DEPTH_TEST)

        asteroidShader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "10.3.asteroids.vs",
                "10.3.planets.fs")

        /*
         * The fun part:
         * generate a large list of semi-random model transformation matrices
         * ------------------------------------------------------------------
         */

        val amount = 100000
        val radius = 150.0
        val offset = 25.0
        val modelMatrices = Array<Matrix4>(amount) {
            var model = Matrix4()
            val angle = it.toDouble() / amount.toDouble() * 360.0
            var displacement = (random() % (2 * offset * 100).toInt()) / 100.0 - offset
            val x = sin(angle) * radius + displacement
            displacement = (random() % (2 * offset * 100).toInt()) / 100.0 - offset
            val y = displacement * 0.4 // keep height of asteroid field smaller compared to x and z
            displacement = (random() % (2 * offset * 100).toInt()) / 100.0 - offset
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
            model
        }

        // configure instanced array
        // -------------------------

        var buffer = IntArray(1)
        glGenBuffers(1, buffer, 0)
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, buffer[0])
        GLES30.glBufferData(GL_ARRAY_BUFFER, amount * 16 * 4, modelMatrices, GL_STATIC_DRAW)  // NEEDS WORKObjFile
        // position attribute
        GLES30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)
        // texture coordinate attribute
        GLES30.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4)
        GLES30.glEnableVertexAttribArray(1)

        // load and create textures
        // -----------------------------------------------------------------------------
        // TODO: flip textures around the y-axis
        texture1 = loadTextureFromAsset163(context,"container2.png")
        texture2= loadTextureFromAsset163(context,"awesomeface.png")

        asteroidShader.use()
        asteroidShader.setInt("texture1", 0)
        asteroidShader.setInt("texture2", 1)

    }

    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")

        camera.zoomHack(-1.0)

        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // (DISABLE THE) Use culling to remove back faces.
        glDisable(GL_CULL_FACE)

        checkGLerr("ODF1")

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture1)
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, texture2)
        asteroidShader.use()

        checkGLerr("ODF2")

        var projectionM4 = Matrix4()

        val zoom = camera.zoom
        projectionM4 = projectionM4.setToPerspective(
                0.1,
                100.0,
                zoom,  //45.0,
                screenWidth.toDouble() / screenHeight.toDouble())
        asteroidShader.setMat4("projection", projectionM4.floatValues)

        // viewM4 = viewM4.translate(Vector3(0.0, 0.0, -3.0))
        val viewM4 = camera.getViewMatrix()
        asteroidShader.setMat4("view", viewM4.floatValues)

        glBindVertexArray(vao[0])

        var i = 0
        while (i < 10) {
            var modelM4 = Matrix4()
            modelM4 = modelM4.translate(cubePositions[i])
            val angle = 20.0 * i
            modelM4 = modelM4.rotate(Vector3(1.0, 0.3, 0.5), angle)
            asteroidShader.setMat4("model", modelM4.floatValues)

            glDrawArrays(GL_TRIANGLES, 0, 36)

            checkGLerr("ODF3")
            i++
        }
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        Timber.i("OnSurfaceChanged, width %d, height %d", width, height )
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
