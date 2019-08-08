package com.androidkotlin.opengl.util

import com.androidkotlin.opengl.math.Matrix4
import com.androidkotlin.opengl.math.Vector3

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
    val floatArray = floatArrayOf(0f, 0f, 0f)
    floatArray[0] = vec3.x.toFloat()
    floatArray[1] = vec3.y.toFloat()
    floatArray[2] = vec3.z.toFloat()
    return floatArray
}
