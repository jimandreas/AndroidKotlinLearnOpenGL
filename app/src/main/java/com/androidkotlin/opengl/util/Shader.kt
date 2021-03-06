package com.androidkotlin.opengl.util

import android.content.Context
import android.opengl.GLES20.*
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

    private var vertexShaderHandle = 0
    private var fragmentShaderHandle = 0
    var programHandle = 0
    enum class ShaderSource { FROM_STRING, FROM_ASSETS }

    fun shaderReadCompileLink(context: Context,
                              sourceFrom: ShaderSource,
                              vertexShaderName: String,
                              fragmentShaderName: String
    ) {

        lateinit var vertexShaderString : String
        lateinit var fragmentShaderString: String
        if (sourceFrom == ShaderSource.FROM_ASSETS) {
            vertexShaderString = context.assets.open(vertexShaderName)
                    .bufferedReader().use {
                        it.readText()
                    }
            fragmentShaderString = context.assets.open(fragmentShaderName)
                    .bufferedReader().use {
                        it.readText()
                    }
        } else {
            vertexShaderString = vertexShaderName
            fragmentShaderString = fragmentShaderName
        }

        vertexShaderHandle = compileShader(GL_VERTEX_SHADER, vertexShaderString, vertexShaderName)

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

                val glError = glGetError()
                if (glError != GL_NO_ERROR) {
                    Timber.e("compileShader, GLERROR: $glError  (0x%X)", glError)
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
            Timber.e("*****************************************************")
            Timber.e("createAndLinkProgram: Error on Shader glCreateProgram")
            throw RuntimeException("Error creating shader program.")
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

        checkGLerr("cALP01")
        // Get the link status.
        val linkStatus = IntArray(1)
        glGetProgramiv(progHandl, GL_LINK_STATUS, linkStatus, 0)

        if (linkStatus[0] == 0) {
            val infoLog = glGetProgramInfoLog(progHandl)
            Timber.e("*****************************************************")
            Timber.e("createAndLinkProgram: Error on Shader glLinkProgram")
            Timber.e("Info log string is: %s", infoLog)
            throw RuntimeException("the glLinkProgram call failed.")
        }

        glDeleteShader(vertexShaderHandle)
        glDeleteShader(fragmentShaderHandle)
//        if (geometryShaderHandle != null) {
//            glDeleteShader(geometryShaderHandle)
//        }
        checkGLerr("cALP02")
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
        val loc = glGetUniformLocation(programHandle, name)
        //Timber.i("*** Loc is $loc")
        glUniform3fv(loc, 1, value, 0)
        checkGLerr("setVec3")
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






}