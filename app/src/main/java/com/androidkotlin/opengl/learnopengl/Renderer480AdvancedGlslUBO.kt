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

// translation of
//   LearnOpenGL-master\src\2.lighting\4.2.lighting_maps_specular_map

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import com.androidkotlin.opengl.realtime.RendererBaseClass
import com.androidkotlin.opengl.ui.ViewModel
import com.androidkotlin.opengl.util.Camera
import com.androidkotlin.opengl.util.Shader
import com.androidkotlin.opengl.util.checkGLerr
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer480AdvancedGlslUBO(
        val context: Context,
        viewModel: ViewModel
) : RendererBaseClass(context, viewModel), GLSurfaceView.Renderer {

    // VertexBufferObject Ids

    private var cubeVBO = IntArray(1)
    private var cubeVAO = IntArray(1)
    private var uboMatrices = IntArray(1)


    private var quadVBO = IntArray(1)
    private var quadVAO = IntArray(1)
    private var instanceVBO = IntArray(1)
    private var patchCount = 0

    private val shader = Shader()
    private val shaderRed = Shader()
    private val shaderGreen = Shader()
    private val shaderBlue = Shader()
    private val shaderYellow = Shader()

    private val camera = Camera(Vector3(0.0, 0.0, 3.0))

    private val quadVertices = floatArrayOf(
            // positions     // colors
            -0.05f, 0.05f, 1.0f, 0.0f, 0.0f,
            0.05f, -0.05f, 0.0f, 1.0f, 0.0f,
            -0.05f, -0.05f, 0.0f, 0.0f, 1.0f,

            -0.05f, 0.05f,  1.0f, 0.0f, 0.0f,
             0.05f, -0.05f, 0.0f, 1.0f, 0.0f,
             0.05f, 0.05f,  0.0f, 1.0f, 1.0f)


    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        checkGLerr("R01")

        shader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "10.1.instancingHack.vs",
                "10.1.instancingHack.fs")

        shaderRed.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "8.advanced_glsl.vs",
                "8.red.fs")

        shaderGreen.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "8.advanced_glsl.vs",
                "8.green.fs")

        shaderBlue.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "8.advanced_glsl.vs",
                "8.blue.fs")

        shaderYellow.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "8.advanced_glsl.vs",
                "8.yellow.fs")

        checkGLerr("R02")

        glGenVertexArrays(1, cubeVAO, 0)
        glGenBuffers(1, cubeVBO, 0)
        glBindVertexArray(cubeVAO[0])
        glBindBuffer(GL_ARRAY_BUFFER, cubeVBO[0])

        val nativeFloatBuffer480 = ByteBuffer
                .allocateDirect(cubeVertices480.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        nativeFloatBuffer480!!.put(cubeVertices480).position(0)
        glBufferData(GL_ARRAY_BUFFER, cubeVertices480.size * 4,
                nativeFloatBuffer480, GLES20.GL_STATIC_DRAW)

        GLES20.glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0)

        checkGLerr("R03")


        // configure a uniform buffer object
        // ---------------------------------
        // first. We get the relevant block indices
        val uniformBlockIndexRed = glGetUniformBlockIndex(shaderRed.programHandle, "Matrices")
        val uniformBlockIndexGreen = glGetUniformBlockIndex(shaderGreen.programHandle, "Matrices")
        val uniformBlockIndexBlue = glGetUniformBlockIndex(shaderBlue.programHandle, "Matrices")
        val uniformBlockIndexYellow = glGetUniformBlockIndex(shaderYellow.programHandle, "Matrices")
        // then we link each shader's uniform block to this uniform binding point
        glUniformBlockBinding(shaderRed.programHandle, uniformBlockIndexRed, 0)
        glUniformBlockBinding(shaderGreen.programHandle, uniformBlockIndexGreen, 0)
        glUniformBlockBinding(shaderBlue.programHandle, uniformBlockIndexBlue, 0)
        glUniformBlockBinding(shaderYellow.programHandle, uniformBlockIndexYellow, 0)

        // Now actually create the buffer
        glGenBuffers(1, uboMatrices, 0)
        glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices[0])
        glBufferData(GL_UNIFORM_BUFFER, 2 * 16 * 4, null, GL_STATIC_DRAW)  // null data
        glBindBuffer(GL_UNIFORM_BUFFER, 0)  // Assign to Buffer 0!!
        // define the range of the buffer that links to a uniform binding point
        glBindBufferRange(GL_UNIFORM_BUFFER, 0, uboMatrices[0], 0, 2 * 16 * 4)

        // view/projection transformations
//        var projection = Matrix4()
//        projection = projection.setToPerspective(
//                0.1,
//                100.0,
//                45.0,
//                screenWidth * 1.0 / screenHeight * 1.0)

        val projection = Matrix4()
        val tempFloatArray = FloatArray(16)
        projection.toFloatArray(tempFloatArray)
        GLES20.glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices[0])
        val nativeFloatBufferProjection = ByteBuffer
                .allocateDirect(16 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        nativeFloatBufferProjection!!.put(tempFloatArray).position(0)
        glBufferSubData(GL_UNIFORM_BUFFER, 0, 16 * 4, nativeFloatBufferProjection)
        glBindBuffer(GL_UNIFORM_BUFFER, 0)

//***************** OLD
        // generate a list of 100 quad locations/translation-vectors
        // ---------------------------------------------------------
        //   buffer is "instanceVBO"
        val offset = 0.01f
        val translationsArray = FloatArray(200)
        var index = 0
        for (y in -5 until -2 step 2) {
            for (x in -5 until -2 step 2) {
                translationsArray[index++] = x.toFloat() / 10.0f + offset
                translationsArray[index++] = y.toFloat() / 10.0f + offset
                translationsArray[index++] = 0f
                patchCount++
            }
        }

        val nativeFloatBuffer2 = ByteBuffer
                .allocateDirect(translationsArray.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        nativeFloatBuffer2!!.put(translationsArray).position(0)
        glGenBuffers(1, instanceVBO, 0)
        glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0])
        glBufferData(GL_ARRAY_BUFFER,
                translationsArray.size * 4,
                nativeFloatBuffer2, GLES20.GL_STATIC_DRAW)
        glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        checkGLerr("R03")

        // set up vertex data (and buffer(s)) and configure vertex attributes
        // ------------------------------------------------------------------


        val nativeFloatBuffer = ByteBuffer
                .allocateDirect(quadVertices.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        nativeFloatBuffer!!.put(quadVertices).position(0)

        glGenVertexArrays(1, quadVAO, 0)
        glGenBuffers(1, quadVBO, 0)
        glBindVertexArray(quadVAO[0])
        glBindBuffer(GL_ARRAY_BUFFER, quadVBO[0])
        glBufferData(GL_ARRAY_BUFFER,
                quadVertices.size * 4,
                nativeFloatBuffer, GLES20.GL_STATIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * 4, 0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * 4, 2 * 4)
        // also set instance data
        glEnableVertexAttribArray(2)
        glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0])
        /*
         * this is the instance coordinate - use one per vertex
         */
        glVertexAttribPointer(2, /*2*/ 3,
                GL_FLOAT,
                false, /*2*/
                3* 4,
                0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glVertexAttribDivisor(2, 1)

        checkGLerr("R04")

// ********** END OLD
    }

    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")

        glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // be sure to activate shader when setting uniforms/drawing objects
        shader.use()
        checkGLerr("ODF01")
        glBindVertexArray(quadVAO[0])
        checkGLerr("ODF02")
        glDrawArraysInstanced(GLES20.GL_TRIANGLES, 0, 6, /*100*/ patchCount)
        checkGLerr("ODF03")
        glBindVertexArray(0)

        //val view = camera.getViewMatrix()
        val view = Matrix4()
        val tempFloatArray = FloatArray(16)
        view.toFloatArray(tempFloatArray)
        GLES20.glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices[0])
        val nativeFloatBufferView = ByteBuffer
                .allocateDirect(16 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        nativeFloatBufferView!!.put(tempFloatArray).position(0)

        glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices[0])
        glBufferSubData(GL_UNIFORM_BUFFER, 16 * 4, 16 * 4, nativeFloatBufferView)
        glBindBuffer(GL_UNIFORM_BUFFER, 0)

        // draw 4 cubes
        // RED
        glBindVertexArray(cubeVAO[0]);
        shaderRed.use()
        var model = Matrix4()
        model = model.translate(Vector3(-0.75, 0.75, 0.0)) // move top-left
        shaderRed.setMat4("model", model.floatValues)
        glDrawArrays(GL_TRIANGLES, 0, 36)
        // GREEN
        shaderGreen.use()
        model = Matrix4()
        model = model.translate(Vector3(0.75, 0.75, 0.0))// move top-right
        shaderGreen.setMat4("model", model.floatValues)
        glDrawArrays(GL_TRIANGLES, 0, 36)
        // YELLOW
        shaderYellow.use()
        model = Matrix4()
        model = model.translate(Vector3(-0.75, -0.75, 0.0))// move bottom-left
        shaderYellow.setMat4("model", model.floatValues)
        glDrawArrays(GL_TRIANGLES, 0, 36)
        // BLUE
        shaderBlue.use()
        model = Matrix4()
        model = model.translate(Vector3(0.75, -0.75, 0.0))// move bottom-right
        shaderBlue.setMat4("model", model.floatValues)
        glDrawArrays(GL_TRIANGLES, 0, 36)

    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)
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

        private var lastX = 0f
        private var lastY = 0f

        private val cubeVertices480 = floatArrayOf(
                // positions
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,

                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,

                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f
                )
    }

}