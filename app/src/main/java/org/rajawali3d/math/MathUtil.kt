/**
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
package org.rajawali3d.math

object MathUtil {
    val PRECISION = 0x020000
    val PI = Math.PI
    val TWO_PI = PI * 2
    val HALF_PI = PI * .5
    val PRE_PI_DIV_180 = PI / 180
    val PRE_180_DIV_PI = 180 / PI

    private val RAD_SLICE = TWO_PI / PRECISION
    private val PRECISION_DIV_2PI = PRECISION / TWO_PI
    private val PRECISION_S = PRECISION - 1
    private val sinTable = DoubleArray(PRECISION)
    private val tanTable = DoubleArray(PRECISION)
    private val isInitialized = initialize()

    fun initialize(): Boolean {
        var rad = 0.0
        for (i in 0 until PRECISION) {
            rad = i * RAD_SLICE
            sinTable[i] = Math.sin(rad)
            tanTable[i] = Math.tan(rad)
        }
        return true
    }

    private fun radToIndex(radians: Double): Int {
        return (radians * PRECISION_DIV_2PI).toInt() and PRECISION_S
    }

    fun sin(radians: Double): Double {
        return sinTable[radToIndex(radians)]
    }

    fun cos(radians: Double): Double {
        return sinTable[radToIndex(HALF_PI - radians)]
    }

    fun tan(radians: Double): Double {
        return tanTable[radToIndex(radians)]
    }

    fun degreesToRadians(degrees: Double): Double {
        return degrees * PRE_PI_DIV_180
    }

    fun radiansToDegrees(radians: Double): Double {
        return radians * PRE_180_DIV_PI
    }

    fun realEqual(a: Double, b: Double, tolerance: Double): Boolean {
        return Math.abs(b - a) <= tolerance
    }

    fun clamp(value: Double, min: Double, max: Double): Double {
        return if (value < min) min else if (value > max) max else value
    }

    fun clamp(value: Int, min: Int, max: Int): Int {
        return if (value < min) min else if (value > max) max else value
    }

    fun clamp(value: Short, min: Short, max: Short): Short {
        return if (value < min) min else if (value > max) max else value
    }

    fun getClosestPowerOfTwo(x: Int): Int {
        var x = x
        --x
        x = x or (x shr 1)
        x = x or (x shr 2)
        x = x or (x shr 4)
        x = x or (x shr 8)
        x = x or (x shr 16)
        return ++x
    }
}
