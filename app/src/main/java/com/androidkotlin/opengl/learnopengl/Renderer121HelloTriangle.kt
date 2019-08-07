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
import com.androidkotlin.opengl.util.Shader
import com.androidkotlin.opengl.util.checkGLerr
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer121HelloTriangle(
        val context: Context,
        viewModel: ViewModel
) : RendererBaseClass(context, viewModel), GLSurfaceView.Renderer {

    private val shaderObject = Shader()

    private var vbo = IntArray(1)
    private var vao = IntArray(1)
    //private var ebo = IntArray(1)

    private val vertexBuffer: FloatBuffer
    // private val indicesBuffer: IntBuffer

    private val vertices = floatArrayOf(
            -0.5f, -0.5f, 0.0f, // left
            0.5f, -0.5f, 0.0f, // right
            0.0f,  0.5f, 0.0f  // top
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

        // initialize byte buffer for the draw list
//        val dlb = ByteBuffer.allocateDirect(
//                // (# of coordinate values * 4 bytes per Int)
//                indices.size * 4)
//        dlb.order(ByteOrder.nativeOrder())
//        indicesBuffer = dlb.asIntBuffer()
//        indicesBuffer.put(indices)
//        indicesBuffer.position(0)

    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Use culling to remove back faces.
        glDisable(GL_CULL_FACE)

        // Enable depth testing
        glEnable(GL_DEPTH_TEST)

        shaderObject.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_STRING,
                vertexShaderSource,
                fragmentShaderSource)

        glGenVertexArrays(1, vao, 0)
        glGenBuffers(1, vbo, 0)
        //glGenBuffers(1, ebo, 0)

        glBindVertexArray(vao[0])
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
        GLES30.glBufferData(GL_ARRAY_BUFFER, vertices.size * 4, vertexBuffer, GL_STATIC_DRAW)
        GLES30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)

        // note that this is allowed, the call to glVertexAttribPointer
        // registered VBO as the vertex attribute's bound vertex buffer object
        // so afterwards we can safely unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        // remember: do NOT unbind the EBO while a VAO is active
        // as the bound element buffer object IS stored in the VAO;
        // keep the EBO bound.
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        // You can unbind the VAO afterwards so other VAO calls
        // won't accidentally modify this VAO, but this rarely happens.
        // Modifying other VAOs requires a call to glBindVertexArray anyway
        // so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
        glBindVertexArray(0)
    }

    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        checkGLerr("ODF1")
        shaderObject.use()
        checkGLerr("ODF2")
        // seeing as we only have a single VAO there's no need
        // to bind it every time, but we'll bind it anyway so to keep things a bit more organized
        glBindVertexArray(vao[0])
        checkGLerr("ODF3")
        glDrawArrays(GL_TRIANGLES, 0, 3)
        //glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
        checkGLerr("ODF4")
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
