package com.androidkotlin.opengl.util

import android.content.Context
import android.opengl.GLES20.*
import timber.log.Timber
import glm_.Java.Companion.glm
import glm_.mat2x2.Mat2
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vector3

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

    private var vertexShaderHandle = 0
    private var fragmentShaderHandle = 0
    private var programHandle = 0

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
            shaderHandle = glCreateShader(shaderType)

            if (shaderHandle != 0) {
                // Pass in the shader string
                glShaderSource(shaderHandle, shaderString)
                if (verbose) Timber.i("attempting to compile %s", shaderName)
                glCompileShader(shaderHandle)
                val compileStatus = IntArray(1)
                glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, compileStatus, 0)

                // If the compilation failed, quit
                if (compileStatus[0] == 0) {
                    Timber.e("Error compiling shader (%s): %s",
                            shaderName,
                            glGetShaderInfoLog(shaderHandle))
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

        val progHandl = glCreateProgram()
        if (progHandl == 0) {
            Thread.currentThread().join()
            throw RuntimeException("Error creating program.")
        }

        // Bind the vertex, fragment and optional geometry shader to the program.
        glAttachShader(progHandl, vertexShaderHandle)
        glAttachShader(progHandl, fragmentShaderHandle)
//        if (geometryShaderHandle != null) {
//            glAttachShader(programHandle, geometryShaderHandle)
//        }

        // Bind attributes
        if (attributes != null) {
            for (i in 0 until attributes.size) {
                glBindAttribLocation(progHandl, i, attributes[i])
            }
        }

        // Link the two/three shaders together into a program.
        glLinkProgram(progHandl)

        // Get the link status.
        val linkStatus = IntArray(1)
        glGetProgramiv(progHandl, GL_LINK_STATUS, linkStatus, 0)

        // If the link failed, delete the program.
        if (linkStatus[0] == 0) {
            Thread.currentThread().join()
            throw RuntimeException("the glLinkProgram call failed.")
        }

        glDeleteShader(vertexShaderHandle)
        glDeleteShader(fragmentShaderHandle)
//        if (geometryShaderHandle != null) {
//            glDeleteShader(geometryShaderHandle)
//        }
        return progHandl
    }

    // utility functions
    //  translations from:
    //  https://github.com/JoeyDeVries/LearnOpenGL/blob/master/includes/learnopengl/shader.h

    fun use() {
        glUseProgram(programHandle)
    }
    fun setInt(name: String, value: Int) {
        glUniform1i(glGetUniformLocation(programHandle, name), value)
    }
    fun setFloat(name: String, value: Float) {
        glUniform1f(glGetUniformLocation(programHandle, name), value)
    }
    fun setVec3(name: String, value: FloatArray) {
        glUniform3fv(glGetUniformLocation(programHandle, name), 1, value, 0)
    }
    fun setMat2(name: String, value: FloatArray) {
        glUniformMatrix2fv(
                glGetUniformLocation(programHandle, name),
                1,
                false,
                value,
                0)
    }
    fun setMat3(name: String, value: FloatArray) {
        glUniformMatrix3fv(
                glGetUniformLocation(programHandle, name),
                1,
                false,
                value,
                0)
    }
    fun setMat4(name: String, value: FloatArray) {
        glUniformMatrix4fv(
                glGetUniformLocation(programHandle, name),
                1,
                false,
                value,
                0)
    }



    /*public static native void glUniformMatrix2fv(
        int location,
        int count,
        boolean transpose,
        float[] value,
        int offset
    );*/





}