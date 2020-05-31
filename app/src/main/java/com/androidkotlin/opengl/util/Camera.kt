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

import org.rajawali3d.math.Matrix
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.Quaternion
import org.rajawali3d.math.WorldParameters
import org.rajawali3d.math.vector.Vector3
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// adaptation of camera code here:
// https://learnopengl.com/code_viewer_gh.php?code=includes/learnopengl/camera.h

/*
 *  basically a straight translation of the learnopengl "camera.h" into kotlin
 */

class Camera {

    /* public */ var position: Vector3 = Vector3(0.0, 0.0, 0.0)
    private var front: Vector3 = Vector3(0.0, 0.0, 0.0)
    private var up: Vector3 = Vector3(0.0, 0.0, 0.0)
    private var right: Vector3 = Vector3(0.0, 0.0, 0.0)
    private var worldUp: Vector3 = Vector3(0.0, 0.0, 0.0)

    // Euler Angles
    private var yaw = YAW
    private var pitch = PITCH

    // camera options
    private var movementSpeed = SPEED
    private var mouseSensitivity = SENSITIVITY
    /* public */ var zoom = ZOOM

    private var tmpOrientation = Quaternion()
    private var orientation = Quaternion()
    private var localOrientation = Quaternion()
    private var cameraViewMatrix = Matrix4()
    private var scratchMatrix = Matrix4()
    private var tempVec = Vector3()

    fun getViewMatrix(): Matrix4 {
        val m = Matrix4()
        cameraViewMatrix = m.setToLookAt(
                position,
                front,
                up
        )
        val quatViewMatrix = getViewMatrixQuaterionBased()

        val eulerViewMatrix = Matrix4()
        Matrix.setLookAtM(eulerViewMatrix.doubleValues, 0, position.x, position.y, position.z,
                front.x, front.y, front.z, up.x, up.y, up.z)

        //return eulerViewMatrix
        return quatViewMatrix
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

    fun setRotation(xIn: Float, yIn: Float) {
        val x = xIn.toDouble()
        val y = yIn.toDouble()
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

    }

    // from :
    //  Rajawali\rajawali\src\main\java\org\rajawali3d\cameras\Camera.java
    //     modificatoins : conversion to Kotlin
    //  Quaternion based camera setup
    private fun getViewMatrixQuaterionBased(): Matrix4 {
        // Create an inverted orientation. This is because the view matrix is the
        // inverse operation of a model matrix
        tmpOrientation.setAll(orientation)
        tmpOrientation.inverse()

        // Create the view matrix
        val matrix = cameraViewMatrix.doubleValues
        // Precompute these factors for speed
        val x2 = tmpOrientation.x * tmpOrientation.x
        val y2 = tmpOrientation.y * tmpOrientation.y
        val z2 = tmpOrientation.z * tmpOrientation.z
        val xy = tmpOrientation.x * tmpOrientation.y
        val xz = tmpOrientation.x * tmpOrientation.z
        val yz = tmpOrientation.y * tmpOrientation.z
        val wx = tmpOrientation.w * tmpOrientation.x
        val wy = tmpOrientation.w * tmpOrientation.y
        val wz = tmpOrientation.w * tmpOrientation.z

        matrix[Matrix4.M00] = 1.0 - 2.0 * (y2 + z2)
        matrix[Matrix4.M10] = 2.0 * (xy - wz)
        matrix[Matrix4.M20] = 2.0 * (xz + wy)
        matrix[Matrix4.M30] = 0.0

        matrix[Matrix4.M01] = 2.0 * (xy + wz)
        matrix[Matrix4.M11] = 1.0 - 2.0 * (x2 + z2)
        matrix[Matrix4.M21] = 2.0 * (yz - wx)
        matrix[Matrix4.M31] = 0.0

        matrix[Matrix4.M02] = 2.0 * (xz - wy)
        matrix[Matrix4.M12] = 2.0 * (yz + wx)
        matrix[Matrix4.M22] = 1.0 - 2.0 * (x2 + y2)
        matrix[Matrix4.M32] = 0.0

        matrix[Matrix4.M03] = (-position.x * matrix[Matrix4.M00]
                + -position.y * matrix[Matrix4.M01] + -position.z * matrix[Matrix4.M02])
        matrix[Matrix4.M13] = (-position.x * matrix[Matrix4.M10]
                + -position.y * matrix[Matrix4.M11] + -position.z * matrix[Matrix4.M12])
        matrix[Matrix4.M23] = (-position.x * matrix[Matrix4.M20]
                + -position.y * matrix[Matrix4.M21] + -position.z * matrix[Matrix4.M22])
        matrix[Matrix4.M33] = 1.0

        tmpOrientation.setAll(localOrientation).inverse()
        cameraViewMatrix.leftMultiply(tmpOrientation.toRotationMatrix(scratchMatrix))
        return cameraViewMatrix

    }

    /**
     * Utility method to move the specified number of units along the current right axis. This will
     * also adjust the look at target (if a valid one is currently set).
     *
     * (NOTE: lookat renamed to "front" to match the learnopengl naming
     *
     * @param unitsIn `double` Number of units to move. If negative, movement will be in the "left" direction.
     */
    fun moveRight(unitsIn: Float) {
        val units = unitsIn.toDouble()
        tempVec.setAll(WorldParameters.RIGHT_AXIS)
        tempVec.rotateBy(orientation).normalize()
        tempVec.multiply(units)
        position.add(tempVec)
        front.add(tempVec)

    }

    fun moveForward(unitsIn: Float) {
        val units = unitsIn.toDouble()
        tempVec.setAll(WorldParameters.FORWARD_AXIS)
        tempVec.rotateBy(orientation).normalize()
        tempVec.multiply(units)
        position.add(tempVec)
        front.add(tempVec)

    }

    fun zoomInOut(deltaZoomIn: Float) : Double {
        val delta = deltaZoomIn.toDouble()
        if (zoom + delta > 45.0) {
            return zoom
        }
        if (zoom + delta < 9.0) {
            return zoom
        }
        zoom += delta
        return zoom
    }

}