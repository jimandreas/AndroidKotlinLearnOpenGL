package com.androidkotlin.opengl.util

import com.androidkotlin.opengl.math.Vector3
import kotlin.math.cos
import kotlin.math.sin


/*
 *  basically a straight translation of "camera.h" into kotlin
 */
class Camera {

    var position : Vector3 = Vector3( 0.0, 0.0, 0.0 )
    var front : Vector3 = Vector3( 0.0, 0.0, 0.0)
    var up : Vector3 = Vector3( 0.0, 0.0, 0.0 )
    var right : Vector3 = Vector3( 0.0, 0.0, 0.0 )
    var worldUp : Vector3 = Vector3( 0.0, 0.0, 0.0 )

    // Euler Angles
    var yaw = YAW
    var pitch = PITCH

    // camera options
    var movementSpeed = SPEED
    var mouseSensitivity = SENSITIVITY
    var zoom = ZOOM

    constructor(positionIn: Vector3) {
        position = positionIn
        up = Vector3(0.0, 1.0, 0)
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
        updateCameraVectors();
    }

    constructor(posX: Double, posY: Double, posZ: Double,
                upX: Double, upY: Double, upZ: Double,
                yawIn: Double, pitchIn: Double) {
        position = Vector3(posX, posY, posZ)
        up = Vector3(upX, upY, upZ)
        yaw = yawIn
        pitch = pitchIn
        front = Vector3(0.0, 0.0, -1.0)
        updateCameraVectors();
    }

    private fun updateCameraVectors() {
        val newfront = Vector3(0.0, 0.0, 0.0)
        newfront.x = cos(Math.toRadians(yaw) * cos(Math.toRadians(pitch)))
        newfront.y = sin(Math.toRadians(pitch))
        newfront.z = sin(Math.toRadians(yaw) * cos(Math.toRadians(pitch)))
        front = newfront.normalize()

        // Also re-calculate the Right and Up vector
        // Normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.

        right = Vector3.normalize(glm.cross(front, worldUp))
        up = Vector3.normalize(glm.cross(right, front))

    }


    fun getViewMatrix(): Mat4 {
        return glm.lookAt(position, position + front, up)
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
    }
}