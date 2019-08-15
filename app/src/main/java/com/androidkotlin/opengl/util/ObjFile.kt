/**
 * For code leveraged from LoaderOBJ.java in Rajawali:
 * Copyright 2013 Dennis Ippel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

@file:Suppress("FunctionName", "LocalVariableName")
package com.androidkotlin.opengl.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLES30
import android.os.Bundle
import android.os.SystemClock
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.math.max
import kotlin.math.min

@SuppressLint("DefaultLocale")
class ObjFile(val context: Context) {
    private val assetManager: AssetManager = context.assets

    private var v1 = FloatArray(3)
    private var v2 = FloatArray(3)
    private var v3 = FloatArray(3)
    private var n = FloatArray(3)

    private var vertices: MutableList<Float> = ArrayList()
    private var normals: MutableList<Float> = ArrayList()
    private var textureCoordinates: MutableList<Float> = ArrayList()
    private var colors: MutableList<Float> = ArrayList()
    private var vertexIndices: MutableList<Short> = ArrayList()
    private var normalIndices: MutableList<Int> = ArrayList()
    private var textureIndices: MutableList<Int> = ArrayList()

    private var haveMaterialColor = false
    private var materialColor = FloatArray(3)
    private var material = Bundle()
    private var triangleIndexCount: Int = 0
    private val vbo = IntArray(1)

    var maxX = 0f
    var maxY = 0f
    var maxZ = 0f
    private var minX = 1e6f
    private var minY = 1e6f
    private var minZ = 1e6f
    private var lastVertexNumber = 0



    fun parse(objFileName: String) {
        // Timber.i("start parsing files = " + objFileName);
        val start_time = SystemClock.uptimeMillis().toFloat()

        flushAllBuffers()
        inputMaterialTemplateLibrary("$objFileName.mtl")
        parseObjFile("$objFileName.obj")

        val elapsed_time = (SystemClock.uptimeMillis() - start_time) / 1000
        val pretty_print = String.format("%6.2f", elapsed_time)

        Timber.i("finished parsing in $pretty_print seconds.")
        Timber.i("max xyz min xyz %7.2f %7.2f %7.2f and %7.2f %7.2f %7.2f",
                maxX, maxY, maxZ, minX, minY, minZ)
    }

    private fun inputMaterialTemplateLibrary(objFileName: String) {
        var inputStream: InputStream? = null
        val reader: BufferedReader?
        var line: String? = null
        try {
            inputStream = assetManager.open(objFileName, AssetManager.ACCESS_BUFFER)
            if (inputStream == null) {
                Timber.i("cannot open$objFileName, returning")
                return
            }

            reader = BufferedReader(InputStreamReader(inputStream))

            var name: String? = null
            line = reader.readLine()
            while (line != null) {
                // Timber.i("line is: " + line);
                if (line.isEmpty()) {
                    line = reader.readLine()
                    continue
                }
                if (line[0] == 'n' && line[1] == 'e') {
                    name = parseMaterialTemplateName(line)
                } else if (line[0] == 'K' && line[1] == 'a') {
                    parseKaColor(name, line)
                }
                line = reader.readLine()
            }
        } catch (e: IOException) {
            Timber.i("IO error in file $objFileName")
            if (line != null) {
                Timber.i("IO exception at line: $line")
            }
        } finally {
            inputStream?.close()
        }
    }

    /*
     * ParseKaColorLine
     *   Assumptions:
     *
     */
    private fun parseKaColor(mat_name: String?, line: String) {

        if (mat_name == null) {
            return
        }
        var first_float = line.substring(3)
        first_float = first_float.trim { it <= ' ' }
        val second_space_index = first_float.indexOf(' ') + 1
        var second_float = first_float.substring(second_space_index)
        second_float = second_float.trim { it <= ' ' }
        val third_space_index = second_float.indexOf(' ') + 1
        var third_float = second_float.substring(third_space_index)
        third_float = third_float.trim { it <= ' ' }

        val color = FloatArray(3)
        color[0] = parseFloat(first_float.substring(0, second_space_index - 1))
        color[1] = parseFloat(second_float.substring(0, third_space_index - 1))
        color[2] = parseFloat(third_float)

        material.putFloatArray(mat_name, color)
        haveMaterialColor = true
    }

    /*
     * ParseMaterialTemplateName
     *   Assumptions:
     *     picking just the Ka will work on the binding between name and color value
     */
    private fun parseMaterialTemplateName(line: String): String {
        val space_index = line.indexOf(' ') + 1
        var mtl_name = line.substring(space_index)
        mtl_name = mtl_name.trim { it <= ' ' }
        return mtl_name
    }

    private fun parseObjFile(objFileName: String) {
        var inputStream: InputStream? = null
        var line: String? = null
        try {
            inputStream = assetManager.open(objFileName, AssetManager.ACCESS_BUFFER)
            if (inputStream == null) {
                Timber.e("cannot open$objFileName, returning")
                return
            }

            val reader = BufferedReader(InputStreamReader(inputStream))
            line = reader.readLine()
            while (line != null) {
                // Timber.i("line is: " + line);
                if (line.isEmpty()) {
                    line = reader.readLine()
                    continue
                }
                if (line[0] == 'v' && line[1] == ' ') {
                    parseVertex(line)
                } else if (line[0] == 'v' && line[1] == 'n') {
                    parseNormal(line)
                } else if (line[0] == 'v' && line[1] == 't') {
                    parseTextureCoordinates(line)
                } else if (line[0] == 'f') {
                    parseFace(line)
                } else if (line[0] == 'u' && line[1] == 's') {
                    parseUsemtl(line)
                }
                line = reader.readLine()
            }
        } catch (e: IOException) {
            Timber.e("IO error in file $objFileName")
            if (line != null) {
                Timber.e("IO exception at line: $line")
            }
        } finally {
            inputStream?.close()
        }

    }

    /**
     * ParseMaterialTemplateName
     * Assumptions:
     * picking just the Ka will work on the binding between name and color value
     */
    private fun parseUsemtl(line: String) {
        val space_index = line.indexOf(' ') + 1
        var mtl_name = line.substring(space_index)
        mtl_name = mtl_name.trim { it <= ' ' }
        val material_color = material.getFloatArray(mtl_name) ?: return
        if (material_color.size != 3) {
            return
        }
        materialColor[0] = material_color[0]
        materialColor[1] = material_color[1]
        materialColor[2] = material_color[2]
        haveMaterialColor = true
    }

    /*
     * ParseTriangle
     *   Assumptions:
     *     exactly one space between the 'v' and the integer
     *     exactly one space between integer
     *
     *     UPDATE : leverages a code snippet from Rajawali
     */
    private fun parseFace(lineIn: String) {
        var line = lineIn
        var parts = StringTokenizer(line, " ")
        val numTokens = parts.countTokens()

        if (numTokens == 0)
            return
        val type = parts.nextToken()

        val isQuad = numTokens == 5
        val quadvids = IntArray(4)
        val quadtids = IntArray(4)
        val quadnids = IntArray(4)

        val emptyVt = line.indexOf("//") > -1
        if (emptyVt) line = line.replace("//", "/")

        parts = StringTokenizer(line)

        parts.nextToken()
        var subParts = StringTokenizer(parts.nextToken(), "/")
        val partLength = subParts.countTokens()

        val hasuv = partLength >= 2 && !emptyVt
        val hasn = partLength == 3 || partLength == 2 && emptyVt
        var idx: Int

        for (i in 1 until numTokens) {
            if (i > 1)
                subParts = StringTokenizer(parts.nextToken(), "/")
            idx = Integer.parseInt(subParts.nextToken())

            if (idx < 0)
                idx += vertices.size / 3
            else
                idx -= 1
            if (!isQuad)
                vertexIndices.add(idx.toShort())
            else
                quadvids[i - 1] = idx
            if (hasuv) {
                idx = Integer.parseInt(subParts.nextToken())
                if (idx < 0) {
                    idx += textureIndices.size / 2
                } else {
                    idx -= 1
                }
                if (!isQuad)
                    textureIndices.add(idx)
                else
                    quadtids[i - 1] = idx
            }
            if (hasn) {
                idx = Integer.parseInt(subParts.nextToken())
                if (idx < 0) {
                    idx += normals.size / 3
                } else
                    idx -= 1
                if (!isQuad)
                    normalIndices.add(idx)
                else
                    quadnids[i - 1] = idx
            }
        }

        if (isQuad) {
            val indices = intArrayOf(0, 1, 2, 0, 2, 3)

            for (i in 0..5) {
                val index = indices[i]
                vertices.add(quadvids[index].toFloat())
                textureIndices.add(quadtids[index])
                normalIndices.add(quadnids[index])
            }
        }

    }

    /**
     * ParseNormal
     * Assumptions:
     * exactly one space between the 'v' and the float
     * exactly one space between floats
     */
    private fun parseNormal(line: String) {

        val first_float = line.substring(3)
        val second_space_index = first_float.indexOf(' ') + 1
        val second_float = first_float.substring(second_space_index)
        val third_space_index = second_float.indexOf(' ') + 1

        val vx = parseFloat(first_float.substring(0, second_space_index - 1))
        val vy = parseFloat(second_float.substring(0, third_space_index - 1))
        val vz = parseFloat(second_float.substring(third_space_index))

        normals.add(vx)
        normals.add(vy)
        normals.add(vz)
    }

    /**
     * ParseTextureCoordinates
     * Assumptions:
     * exactly one space between the 'v' and the float
     * exactly one space between floats
     */
    private fun parseTextureCoordinates(line: String) {

        val first_float = line.substring(3)
        val second_space_index = first_float.indexOf(' ') + 1
        val second_float = first_float.substring(second_space_index)

        val tu = parseFloat(first_float.substring(0, second_space_index - 1))
        val tv = parseFloat(second_float)

        textureCoordinates.add(tu)
        textureCoordinates.add(tv)
    }

    /*
     * ParseVertex
     *   Assumptions:
     *     exactly one space between the 'v' and the float
     *     exactly one space between floats
     */
    private fun parseVertex(line: String) {

        var first_float = line.substring(2)
        first_float = first_float.trim { it <= ' ' }
        val second_space_index = first_float.indexOf(' ') + 1
        var second_float = first_float.substring(second_space_index)
        second_float = second_float.trim { it <= ' ' }
        val third_space_index = second_float.indexOf(' ') + 1
        var third_float = second_float.substring(third_space_index)
        third_float = third_float.trim { it <= ' ' }

        val vx = parseFloat(first_float.substring(0, second_space_index - 1))
        val vy = parseFloat(second_float.substring(0, third_space_index - 1))
        val vz = parseFloat(third_float)

        maxX = max(maxX, vx)
        maxY = max(maxY, vy)
        maxZ = max(maxZ, vz)

        minX = min(minX, vx)
        minY = min(minY, vy)
        minZ = min(minZ, vz)

        vertices.add(vx)
        vertices.add(vy)
        vertices.add(vz)
        lastVertexNumber++

        if (haveMaterialColor) {
            colors.add(materialColor[0])
            colors.add(materialColor[1])
            colors.add(materialColor[2])
        }
    }

    private fun parseFloat(s: String): Float {
        return try {
            java.lang.Float.parseFloat(s)
        } catch (e: RuntimeException) {
            0f
        }

    }

    private fun parseInteger(s: String): Int {
        return try {
            Integer.parseInt(s)
        } catch (e: RuntimeException) {
            Timber.e("Bad Integer : $s")
            0
        }

    }

    /*
     * pull the data from the buffers and assemble
     * a packed VBO (vertex + normal + texture coordinates) buffer,
     * and an vertexIndices buffer.
     *
     * Walk the vertexIndices list
     * to pull the triangle vertices,
     * pull the normal and text coordinates
     * and stuff them back into the packed VBO.
     *
     */
    fun build_buffers() {
        var offset = 0

        val vertexData = FloatArray(vertexIndices.size * STRIDE_IN_FLOATS)

        /*
         * loop to generate vertices + normals + texture coordinates
         */
        val size = vertexIndices.size
        for (i in 0 until size) {

            if (offset + 7 > vertexData.size) {
                Timber.e("out of range on index %d > size: %d",
                        offset, vertexData.size)
                break
            }
            val vertexNum = vertexIndices[i]
            if (vertexNum * 3 + 2 > vertices.size) {
                Timber.e("out of range on vertices %d > size: %d",
                        vertexNum * 3 + 2, vertices.size)
                continue
            }
            vertexData[offset++] = vertices[vertexNum * 3 + 0]
            vertexData[offset++] = vertices[vertexNum * 3 + 1]
            vertexData[offset++] = vertices[vertexNum * 3 + 2]

            val normalNum = normalIndices[i]
            if (normalNum * 3 + 1 > normals.size) {
                Timber.e("out of range on normals %d > size: %d",
                        normalNum * 3 + 2, normals.size)
                continue
            }
            vertexData[offset++] = normals[normalNum * 3 + 0]
            vertexData[offset++] = normals[normalNum * 3 + 1]
            vertexData[offset++] = normals[normalNum * 3 + 2]

            val textureNum = textureIndices[i]
            if (textureNum * 2 + 1 > textureCoordinates.size) {
                Timber.e("out of range on textureCoordinates %d > size: %d",
                        textureNum * 2 + 1, textureCoordinates.size)
                continue
            }
            vertexData[offset++] = textureCoordinates[textureNum * 2 + 0]
            vertexData[offset++] = textureCoordinates[textureNum * 2 + 1]
        }

        val vertexDataBuffer = ByteBuffer
                .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        vertexDataBuffer.put(vertexData).position(0)
        checkGLerr("ObjFile: build_buffers1")
        Timber.i("ObjFile: About to BindBuffer in OBJ")
        if (vbo[0] > 0) {
            glDeleteBuffers(1, vbo, 0)
        }
        glGenBuffers(1, vbo, 0)
        if (vbo[0] > 0) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
            glBufferData(GL_ARRAY_BUFFER, vertexData.size * BYTES_PER_FLOAT,
                    vertexDataBuffer, GLES20.GL_STATIC_DRAW)

            // GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        } else {
            checkGLerr("buildBuffers END")
            throw RuntimeException("error on buffer gen")
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        checkGLerr("ObjFile: build_buffers: End")
    }

    fun getVBO(): IntArray {
        return vbo
    }

    fun getVertexCount(): Int {
        return vertexIndices.size
    }

    fun render(shaderObj : Shader) {

        // Debug: disable culling to remove back faces.
        //GLES20.glDisable(GLES20.GL_CULL_FACE)

        if (vbo[0] > 0) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
            // position attribute
            GLES30.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 8 * 4, 0)
            GLES30.glEnableVertexAttribArray(0)
            // texture coordinate attribute
            GLES30.glVertexAttribPointer(2, 2, GLES20.GL_FLOAT, false, 8 * 4, 6 * 4)
            GLES30.glEnableVertexAttribArray(2)


            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexIndices.size)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            checkGLerr("ObjFile: render")
        }

        // Debug:  Use culling to remove back faces.
        //GLES20.glEnable(GLES20.GL_CULL_FACE)
    }

    fun release() {
        if (vbo[0] > 0) {
            glDeleteBuffers(vbo.size, vbo, 0)
            vbo[0] = 0
        }
    }

    // clean out old data, reset state
    private fun flushAllBuffers() {
        maxX = 0f
        maxY = 0f
        maxZ = 0f
        minX = 1e6f
        minY = 1e6f
        minZ = 1e6f
        lastVertexNumber = 0 // zero based counting :-)
        vertices.clear()
        normals.clear()
        colors.clear()
        vertexIndices.clear()
        textureIndices.clear()
        haveMaterialColor = false
    }

    companion object {
        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val TEXTURE_DATA_SIZE_IN_ELEMENTS = 2

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + TEXTURE_DATA_SIZE_IN_ELEMENTS
    }

}
