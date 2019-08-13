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
    private val planet = ObjFile(context)

    private val camera = Camera(Vector3(0.0, 0.0, 155.0))

//    private var vbo = IntArray(1)
//    private var vao = IntArray(1)
//    //private var ebo = IntArray(1)

    private val vertexBuffer: FloatBuffer
    private var planetDiffuseTextureMap = 0


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
                "10.3.asteroids.fs")

        planetShader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "10.3.planet.vs",
                "10.3.planet.fs")

        // load textures
        // -----------------------------------------------------------------------------
        planetDiffuseTextureMap = loadTextureFromAsset(context,"container2.png")

        /*
         * generate a large list of semi-random model transformation matrices
         * ------------------------------------------------------------------
         */

        val amount = 100000
        val radius = 150.0
        val offset = 25.0
        val modelMatrices = Array(amount) {
            var model = Matrix4()
            val angle = it.toDouble() / amount.toDouble() * 360.0
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
            model
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
        GLES30.glBufferData(GL_ARRAY_BUFFER, amount * 16 * 4, nativeFloatBuffer, GL_STATIC_DRAW)
        checkGLerr("OSC1")

        // set transformation matrices as an instance vertex attribute (with divisor 1)
        // note: we're cheating a little by taking the publicly declared
        // VAO of the model's mesh(es) and adding new vertexAttribPointers
        // normally you'd want to do this in a more organized fashion,
        // but for learning purposes this will do.
        // ------------------------------------------------------------------------------

        planet.parse("planet")
        planet.build_buffers()
    }

    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")

        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // (DISABLE THE) Use culling to remove back faces.
        glDisable(GL_CULL_FACE)

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
        asteroidShader.use()
        asteroidShader.setMat4("projection", toFloatArray16(projection))
        asteroidShader.setMat4("view", toFloatArray16(view))

        planetShader.use()
        planetShader.setMat4("projection", toFloatArray16(projection))
        planetShader.setMat4("view", toFloatArray16(view))
        planetShader.setInt("texture_diffuse1", planetDiffuseTextureMap)

        var model = Matrix4() // identity matrix
        model = model.translate(Vector3(0.0, -3.0, 0.0))
        model = model.scale(Vector3(4.0, 4.0, 4.0))
        planetShader.setMat4("model", toFloatArray16(model))
        planet.render(planetShader)
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
