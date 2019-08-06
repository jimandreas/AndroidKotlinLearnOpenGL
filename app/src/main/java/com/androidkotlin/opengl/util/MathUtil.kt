package com.androidkotlin.opengl.util

import com.androidkotlin.opengl.math.Matrix4
import com.androidkotlin.opengl.math.Vector3

fun toFloatArray16(someMatrix: Matrix4) : FloatArray {
    val floatA= floatArrayOf(
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f
            )
    someMatrix to floatA
    return floatA
}

fun toVec3(someFloatArray: FloatArray): Vector3 {
    val vec3 = Vector3(0.0, 0.0, 0.0)
    someFloatArray to vec3
    return vec3
}

fun toFloatArray3(vec3: Vector3): FloatArray {
    val floatArray = floatArrayOf(0f, 0f, 0f)
    vec3 to floatArray
    return floatArray
}
