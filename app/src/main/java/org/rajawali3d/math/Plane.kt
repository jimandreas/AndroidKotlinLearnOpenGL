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
@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package org.rajawali3d.math

import org.rajawali3d.math.vector.Vector3

class Plane {
    /**
     * Plane normal
     */
    var normal: Vector3 = Vector3(0.0, 0.0, 0.0)
        private set
    var d: Double = 0.toDouble()
        private set

    enum class PlaneSide {
        BACK, ONPLANE, FRONT
    }

    constructor() {
        normal = Vector3()
    }

    /**
     * Create a plane from coplanar points
     *
     * @param point1
     * @param point2
     * @param point3
     */
    constructor(point1: Vector3, point2: Vector3, point3: Vector3) {
        set(point1, point2, point3)
    }

    operator fun set(point1: Vector3, point2: Vector3, point3: Vector3) {
        val v1 = Vector3()
        val v2 = Vector3()
        v1.subtractAndSet(point1, point2)
        v2.subtractAndSet(point3, point2)
        normal = v1.cross(v2)
        normal.normalize()

        d = -point1.dot(normal)
    }

    fun setComponents(x: Double, y: Double, z: Double, w: Double) {
        normal.setAll(x, y, z)
        d = w
    }

    fun getDistanceTo(point: Vector3): Double {
        return d + normal.dot(point)
    }

    fun getPointSide(point: Vector3): PlaneSide {
        val distance = Vector3.dot(normal, point) + d
        return when {
            distance == 0.0 -> PlaneSide.ONPLANE
            distance < 0 -> PlaneSide.BACK
            else -> PlaneSide.FRONT
        }
    }

    fun isFrontFacing(direction: Vector3): Boolean {
        val dot = Vector3.dot(normal, direction)
        return dot <= 0
    }

    fun normalize() {
        val inverseNormalLength = 1.0 / normal.length()
        normal.multiply(inverseNormalLength)
        d *= inverseNormalLength
    }
}
