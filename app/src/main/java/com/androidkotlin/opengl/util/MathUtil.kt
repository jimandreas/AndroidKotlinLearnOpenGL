package com.androidkotlin.opengl.util

import android.opengl.GLES20
import android.opengl.GLES30
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

fun toFloatArray16(someMatrix: Matrix4) : FloatArray {
    return someMatrix.floatValues
}

fun toVec3(someFloatArray: FloatArray): Vector3 {
    val retVal = Vector3()
    retVal.setAll(
            someFloatArray[0].toDouble(),
            someFloatArray[1].toDouble(),
            someFloatArray[2].toDouble()
    )
    return retVal
}

fun toFloatArray3(vec3: Vector3): FloatArray {
    val doubleArray = vec3.toArray()!!
    val floatArray = floatArrayOf(
        doubleArray[0].toFloat(),
        doubleArray[1].toFloat(),
        doubleArray[2].toFloat()
    )
    return floatArray
}

fun matrix4toFloatArray(matrix4array: List<Matrix4>): FloatArray {
    val size = matrix4array.size
    val outputFloatArray = FloatArray(size * 16)

    val tempArray = FloatArray(16)
    for (i in 0 until size) {
        matrix4array[i].toFloatArray(tempArray)
        for (j in 0 until 16) {
            outputFloatArray[i*16 + j] = tempArray[j]
        }
    }
    return outputFloatArray
}

/**
 * toss a Matrix4 into a NIO native buffer (FloatBuffer) and return it
 */
fun m4toFloatBuffer(m4: Matrix4) : FloatBuffer {
    val tempFloatArray = FloatArray(16)
    m4.toFloatArray(tempFloatArray)
    val nativeFloatBuffer = ByteBuffer
            .allocateDirect(16 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
    nativeFloatBuffer!!.put(tempFloatArray).position(0)
    return nativeFloatBuffer
}