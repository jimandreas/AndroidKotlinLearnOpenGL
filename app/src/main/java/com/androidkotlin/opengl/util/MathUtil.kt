package com.androidkotlin.opengl.util

import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3

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
