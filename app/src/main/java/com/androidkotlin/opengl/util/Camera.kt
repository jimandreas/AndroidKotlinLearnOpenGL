/*
 * All code samples, unless explicitly stated otherwise,
 * are licensed under the terms of the CC BY-NC 4.0 license
 * as published by Creative Commons, either version 4 of the License,
 * or (at your option) any later version. You can find a human-readable
 * format of the license here:
 * https://creativecommons.org/licenses/by-nc/4.0/
 * and the full license here:
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Adaptation of:  https://learnopengl.com/Getting-started/Camera
 * Translation to kotlin and adaptation to android architecture:
 * Jim Andreas  jim@jimandreas.com
 */

@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "unused_property",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "PropertyName")

package com.androidkotlin.opengl.util

import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

// adaptation of camera code here:
// https://learnopengl.com/code_viewer_gh.php?code=includes/learnopengl/camera.h

/*
 *  basically a straight translation of "camera.h" into kotlin
 */
class Camera {

    var position: Vector3 = Vector3(0.0, 0.0, 0.0)
    var front: Vector3 = Vector3(0.0, 0.0, 0.0)
    var up: Vector3 = Vector3(0.0, 0.0, 0.0)
    var right: Vector3 = Vector3(0.0, 0.0, 0.0)
    var worldUp: Vector3 = Vector3(0.0, 0.0, 0.0)

    // Euler Angles
    var yaw = YAW
    var pitch = PITCH

    // camera options
    var movementSpeed = SPEED
    var mouseSensitivity = SENSITIVITY
    var zoom = ZOOM


    fun getViewMatrix(): Matrix4 {
        val m = Matrix4()
        val viewMatrix = m.setToLookAt(
                position,
                front,
                up
        )
        return viewMatrix
    }

    constructor(positionIn: Vector3) {
        position = positionIn
        up = Vector3(0.0, 1.0, 0.0)
        worldUp = Vector3(0.0, 1.0, 0.0)
        yaw = YAW
        pitch = PITCH
        front = Vector3(0.0, 0.0, -1.0)
        movementSpeed = SPEED
        zoom = ZOOM
        updateCameraVectors()
    }

    constructor(positionIn: Vector3,
                upIn: Vector3,
                yawIn: Double,
                pitchIn: Double,
                frontIn: Vector3,
                movementSpeedIn: Double,
                zoomIn: Double) {
        position = positionIn
        up = upIn
        worldUp = upIn.clone()
        yaw = yawIn
        pitch = pitchIn
        front = frontIn
        movementSpeed = movementSpeedIn
        zoom = zoomIn
        updateCameraVectors()
    }

    constructor(posX: Double, posY: Double, posZ: Double,
                upX: Double, upY: Double, upZ: Double,
                yawIn: Double, pitchIn: Double) {
        position = Vector3(posX, posY, posZ)
        up = Vector3(upX, upY, upZ)
        worldUp = up.clone()
        yaw = yawIn
        pitch = pitchIn
        front = Vector3(0.0, 0.0, -1.0)
        updateCameraVectors()
    }

    private fun updateCameraVectors() {
        val newfront = Vector3(
                cos(Math.toRadians(yaw) * cos(Math.toRadians(pitch))),
                sin(Math.toRadians(pitch)),
                sin(Math.toRadians(yaw) * cos(Math.toRadians(pitch))))
        newfront.normalize()
        front = newfront.clone()
        val tempFront1 = front.clone()
        val tempFront2 = front.clone()

        // Also re-calculate the Right and Up vector
        // Normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.

        right = tempFront1.cross(worldUp)
        right.normalize()
        val tempRight = right.clone()
        up = tempRight.cross(tempFront2)
        up.normalize()

    }

    fun normalize(p1: FloatArray): FloatArray {
        val mag = sqrt((p1[0] * p1[0] +
                p1[1] * p1[1] +
                p1[2] * p1[2]).toDouble()).toFloat()
        T[0] = p1[0] / mag
        T[1] = p1[1] / mag
        T[2] = p1[2] / mag
        return T
    }

    fun setRotation(x: Double, y: Double) {
        val scale = 50.0
        val sx = x / scale
        val sy = y / scale
        val newPosition = front.multiply(sy)
        position = position.add(newPosition)
        val newDirection = right.multiply(sx)
        position = position.add(newDirection)
        updateCameraVectors()
    }

    companion object {

        enum class CameraMovement {
            FORWARD,
            BACKWARD,
            LEFT,
            RIGHT
        }

        const val YAW = -90.0
        const val PITCH = 0.0
        const val SPEED = 2.5
        const val SENSITIVITY = 0.1
        const val ZOOM = 45.0


        private val U = FloatArray(3)
        private val V = FloatArray(3)
        private val N = FloatArray(3)
        private val T = FloatArray(3)

        // https://www.opengl.org/wiki/Calculating_a_Surface_Normal
        fun getNormal(p1: FloatArray, p2: FloatArray, p3: FloatArray): FloatArray {
            U[0] = p2[0] - p1[0]
            U[1] = p2[1] - p1[1]
            U[2] = p2[2] - p1[2]

            V[0] = p3[0] - p1[0]
            V[1] = p3[1] - p1[1]
            V[2] = p3[2] - p1[2]

            N[0] = U[1] * V[2] - U[2] * V[1]
            N[1] = U[2] * V[0] - U[0] * V[2]
            N[2] = U[0] * V[1] - U[1] * V[0]
            return normalize(N)
        }

        private fun normalize(p1: FloatArray): FloatArray {
            val mag = sqrt((p1[0] * p1[0] +
                    p1[1] * p1[1] +
                    p1[2] * p1[2]).toDouble()).toFloat()
            T[0] = p1[0] / mag
            T[1] = p1[1] / mag
            T[2] = p1[2] / mag
            return T
        }

        /*fun normalize(p1: Vector3): Vector3 {
            val mag = sqrt(
                    p1.x * p1.x +
                            p1.y * p1.y +
                            p1.z * p1.z)
            p1.x = p1.x / mag
            p1.y = p1.y / mag
            p1.z = p1.z / mag
            return p1
        }*/

    }
}