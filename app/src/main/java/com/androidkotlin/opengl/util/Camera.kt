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

import com.androidkotlin.opengl.math.Vector3
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


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

    constructor(positionIn: Vector3) {
        position = positionIn
        up = Vector3(0.0, 1.0, 0.0)
        yaw = YAW
        pitch = PITCH
        front = Vector3(0.0, 0.0, -1.0)
        movementSpeed = SPEED
        zoom = ZOOM
        updateCameraVectors()
    }

    constructor(positionIn: Vector3, upIn: Vector3, yawIn: Double, pitchIn: Double,
                frontIn: Vector3, movementSpeedIn: Double, zoomIn: Double) {
        position = positionIn
        up = upIn
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
        yaw = yawIn
        pitch = pitchIn
        front = Vector3(0.0, 0.0, -1.0)
        updateCameraVectors()
    }

    private fun updateCameraVectors() {
        val newfront = Vector3(0.0, 0.0, 0.0)
        newfront.x = cos(Math.toRadians(yaw) * cos(Math.toRadians(pitch)))
        newfront.y = sin(Math.toRadians(pitch))
        newfront.z = sin(Math.toRadians(yaw) * cos(Math.toRadians(pitch)))
        front = normalize(front)

        // Also re-calculate the Right and Up vector
        // Normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.

        right = normalize(front.cross(worldUp))
        up = normalize(right.cross(right))

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


    /*fun getViewMatrix(): Matrix4 {
        return Matrix4.setToLookAt(position, position + front, up)
    }
*/
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

        fun normalize(p1: FloatArray): FloatArray {
            val mag = sqrt((p1[0] * p1[0] +
                    p1[1] * p1[1] +
                    p1[2] * p1[2]).toDouble()).toFloat()
            T[0] = p1[0] / mag
            T[1] = p1[1] / mag
            T[2] = p1[2] / mag
            return T
        }

        fun normalize(p1: Vector3): Vector3 {
            val mag = sqrt(sqrt(
                    p1.x * p1.x +
                            p1.y * p1.y +
                            p1.z * p1.z))
            p1.x = p1.x / mag
            p1.y = p1.y / mag
            p1.z = p1.z / mag
            return p1
        }

    }
}