package com.androidkotlin.opengl.util

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_VERTEX_SHADER
import timber.log.Timber

/*
 * A rough translation of "shader.h" into Kotlin:
 * https://github.com/JoeyDeVries/LearnOpenGL/blob/master/includes/learnopengl/shader.h
 *
 * NOTE: Geometry shaders do not appear in the GLES20 file at this point.
 *    Therefore refs to geometry shader support is commented out
 * License / copyright:
 * https://github.com/JoeyDeVries/LearnOpenGL/blob/master/LICENSE.md
 */

class Shader {

    var vertexShaderHandle = 0
    var fragmentShaderHandle = 0
    var programHandle = 0

    fun shaderReadCompileLink(context: Context,
                              vertexShaderName: String,
                              fragmentShaderName: String
                      ) {

        val vertexShaderString = context.assets.open(vertexShaderName)
                .bufferedReader().use {
                    it.readText()
                }
        vertexShaderHandle = compileShader(GL_VERTEX_SHADER, vertexShaderString, vertexShaderName)
        val fragmentShaderString = context.assets.open(fragmentShaderName)
                .bufferedReader().use {
                    it.readText()
                }
        fragmentShaderHandle = compileShader(GL_FRAGMENT_SHADER, fragmentShaderString, fragmentShaderName)

        programHandle = createAndLinkProgram(
                vertexShaderHandle,
                fragmentShaderHandle,
                null )

        // return programHandle
    }

    /*
     * Helper function to compile a shader.
     *
     * shaderType   The shader type.
     * shaderString The shader source code.
     * return an OpenGL handle to the shader.
     */
    private fun compileShader(shaderType: Int,
                              shaderString: String,
                              shaderName : String): Int {
        val verbose = true
        var shaderHandle = 0
        try {
            shaderHandle = GLES20.glCreateShader(shaderType)

            if (shaderHandle != 0) {
                // Pass in the shader string
                GLES20.glShaderSource(shaderHandle, shaderString)
                if (verbose) Timber.i("attempting to compile %s", shaderName)
                GLES20.glCompileShader(shaderHandle)
                val compileStatus = IntArray(1)
                GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

                // If the compilation failed, quit
                if (compileStatus[0] == 0) {
                    Timber.e("Error compiling shader (%s): %s",
                            shaderName,
                            GLES20.glGetShaderInfoLog(shaderHandle))
                    Thread.currentThread().join()
                    throw RuntimeException("Error creating shader.")

                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception in compile Shader")
            Thread.currentThread().join()
        }

        if (shaderHandle == 0) {
            Thread.currentThread().join()
            throw RuntimeException("Error creating shader, handle is zero!!.")
        }

        return shaderHandle
    }

    /*
     * Helper function to compile and link a program.
     *
     * vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * attributes           Attributes that need to be bound to the program.
     * return an OpenGL handle to the program.
     */
    private fun createAndLinkProgram(
            vertexShaderHandle: Int,
            fragmentShaderHandle: Int,
            attributes: Array<String>?): Int {

        val programHandle = GLES20.glCreateProgram()
        if (programHandle == 0) {
            Thread.currentThread().join()
            throw RuntimeException("Error creating program.")
        }

        // Bind the vertex, fragment and optional geometry shader to the program.
        GLES20.glAttachShader(programHandle, vertexShaderHandle)
        GLES20.glAttachShader(programHandle, fragmentShaderHandle)
//        if (geometryShaderHandle != null) {
//            GLES20.glAttachShader(programHandle, geometryShaderHandle)
//        }

        // Bind attributes
        if (attributes != null) {
            for (i in 0 until attributes.size) {
                GLES20.glBindAttribLocation(programHandle, i, attributes[i])
            }
        }

        // Link the two/three shaders together into a program.
        GLES20.glLinkProgram(programHandle)

        // Get the link status.
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)

        // If the link failed, delete the program.
        if (linkStatus[0] == 0) {
            Thread.currentThread().join()
            throw RuntimeException("the glLinkProgram call failed.")
        }

        GLES20.glDeleteShader(vertexShaderHandle)
        GLES20.glDeleteShader(fragmentShaderHandle)
//        if (geometryShaderHandle != null) {
//            GLES20.glDeleteShader(geometryShaderHandle)
//        }
        return programHandle
    }

}