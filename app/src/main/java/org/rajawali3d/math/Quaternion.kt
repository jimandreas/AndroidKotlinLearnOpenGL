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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "LocalVariableName")
package org.rajawali3d.math

import androidx.annotation.FloatRange
import androidx.annotation.NonNull
import androidx.annotation.Size
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.math.vector.Vector3.Axis
import kotlin.math.*

/**
 * Encapsulates a quaternion.
 *
 * Ported from http://www.ogre3d.org/docs/api/html/classOgre_1_1Quaternion.html
 *
 * Rewritten July 27, 2013 by Jared Woolston with heavy influence from libGDX
 *
 * @author dennis.ippel
 * @author Jared Woolston (jwoolston@tenkiv.com)
 * @author Dominic Cerisano (Quaternion camera lookAt)
 * @see [https
 * ://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
 *
 * @see [https://users.aalto.fi/~ssarkka/pub/quat.pdf](https://users.aalto.fi/~ssarkka/pub/quat.pdf)
 */

class Quaternion : Cloneable {

    //The Quaternion components
    var w: Double = 0.toDouble()
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()
    var z: Double = 0.toDouble()

    //Scratch members
    @NonNull
    private val mTmpVec1 = Vector3()
    @NonNull
    private val mTmpVec2 = Vector3()
    @NonNull
    private val mTmpVec3 = Vector3()

    /**
     * Creates a [Vector3] which represents the x axis of this [Quaternion].
     *
     * @return [Vector3] The x axis of this [Quaternion].
     */
    val xAxis: Vector3
        @NonNull
        get() {
            val fTy = 2.0 * y
            val fTz = 2.0 * z
            val fTwy = fTy * w
            val fTwz = fTz * w
            val fTxy = fTy * x
            val fTxz = fTz * x
            val fTyy = fTy * y
            val fTzz = fTz * z

            return Vector3(1 - (fTyy + fTzz), fTxy + fTwz, fTxz - fTwy)
        }

    /**
     * Creates a [Vector3] which represents the y axis of this [Quaternion].
     *
     * @return [Vector3] The y axis of this [Quaternion].
     */
    val yAxis: Vector3
        @NonNull
        get() {
            val fTx = 2.0 * x
            val fTy = 2.0 * y
            val fTz = 2.0 * z
            val fTwx = fTx * w
            val fTwz = fTz * w
            val fTxx = fTx * x
            val fTxy = fTy * x
            val fTyz = fTz * y
            val fTzz = fTz * z

            return Vector3(fTxy - fTwz, 1 - (fTxx + fTzz), fTyz + fTwx)
        }

    /**
     * Creates a [Vector3] which represents the z axis of this [Quaternion].
     *
     * @return [Vector3] The z axis of this [Quaternion].
     */
    val zAxis: Vector3
        @NonNull
        get() {
            val fTx = 2.0 * x
            val fTy = 2.0 * y
            val fTz = 2.0 * z
            val fTwx = fTx * w
            val fTwy = fTy * w
            val fTxx = fTx * x
            val fTxz = fTz * x
            val fTyy = fTy * y
            val fTyz = fTz * y

            return Vector3(fTxz + fTwy, fTyz - fTwx, 1 - (fTxx + fTyy))
        }

    /**
     * Get the pole of the gimbal lock, if any. Requires that this [Quaternion] be normalized.
     *
     * @return positive (+1) for north pole, negative (-1) for south pole, zero (0) when no gimbal lock
     *
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val gimbalPole: Int
        //@IntRange(from = -1, to = 1)
        get() {
            val t = y * x + z * w
            return if (t > 0.499) 1 else if (t < -0.499) -1 else 0
        }

    /**
     * Gets the pitch angle from this [Quaternion]. This is defined as the rotation about the X axis. Requires
     * that this [Quaternion] be normalized.
     *
     * @return double The pitch angle in radians.
     *
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val rotationX: Double
        get() {
            val pole = gimbalPole
            return if (pole == 0) asin(MathUtil.clamp(2.0 * (w * x - z * y), -1.0, 1.0)) else pole.toDouble() * MathUtil.PI * 0.5
        }

    /**
     * Gets the yaw angle from this [Quaternion]. This is defined as the rotation about the Y axis. Requires that
     * this [Quaternion] be normalized.
     *
     * @return double The yaw angle in radians.
     *
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val rotationY: Double
        get() = if (gimbalPole == 0) atan2(2.0 * (y * w + x * z), 1.0 - 2.0 * (y * y + x * x)) else 0.0

    /**
     * Gets the roll angle from this [Quaternion]. This is defined as the rotation about the Z axis. Requires that
     * this [Quaternion] be normalized.
     *
     * @return double The roll angle in radians.
     *
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val rotationZ: Double
        get() {
            val pole = gimbalPole
            return if (pole == 0)
                atan2(2.0 * (w * z + y * x), 1.0 - 2.0 * (x * x + z * z))
            else
                pole.toDouble() * 2.0 * atan2(y, w)
        }

    /**
     * Default constructor. Creates an identity [Quaternion].
     */
    constructor() {
        identity()
    }

    /**
     * Creates a [Quaternion] with the specified components.
     *
     * @param w double The w component.
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     */
    constructor(w: Double, x: Double, y: Double, z: Double) {
        setAll(w, x, y, z)
    }

    /**
     * Creates a [Quaternion] with components initialized by the provided
     * [Quaternion].
     *
     * @param quat [Quaternion] to take values from.
     */
    constructor(@NonNull quat: Quaternion) {
        setAll(quat)
    }

    /**
     * Creates a [Quaternion] from the given axis vector and the rotation
     * angle around the axis.
     *
     * @param axis  [Vector3] The axis of rotation.
     * @param angle double The angle of rotation in degrees.
     */
    constructor(@NonNull axis: Vector3, angle: Double) {
        fromAngleAxis(axis, angle)
    }

    /**
     * Sets the components of this [Quaternion].
     *
     * @param w double The w component.
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun setAll(w: Double, x: Double, y: Double, z: Double): Quaternion {
        this.w = w
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    /**
     * Sets the components of this [Quaternion] from those
     * of the provided [Quaternion].
     *
     * @param quat [Quaternion] to take values from.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun setAll(quat: Quaternion): Quaternion {
        return setAll(quat.w, quat.x, quat.y, quat.z)
    }

    /**
     * Sets this [Quaternion]'s components from the given axis and angle around the axis.
     *
     * @param axis  [Axis] The cardinal axis to set rotation on.
     * @param angle double The rotation angle in degrees.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromAngleAxis(@NonNull axis: Axis, angle: Double): Quaternion {
        fromAngleAxis(Vector3.getAxisVector(axis), angle)
        return this
    }

    /**
     * Sets this [Quaternion]'s components from the given axis vector and angle around the axis.
     *
     * @param axis  [Vector3] The axis to set rotation on.
     * @param angle double The rotation angle in degrees.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromAngleAxis(@NonNull axis: Vector3, angle: Double): Quaternion {
        if (axis.isZero) {
            return identity()
        } else {
            mTmpVec1.setAll(axis)
            if (!mTmpVec1.isUnit) {
                mTmpVec1.normalize()
            }
            val radian = MathUtil.degreesToRadians(angle)
            val halfAngle = radian * .5
            val halfAngleSin = sin(halfAngle)
            w = cos(halfAngle)
            x = halfAngleSin * mTmpVec1.x
            y = halfAngleSin * mTmpVec1.y
            z = halfAngleSin * mTmpVec1.z
            return this
        }
    }

    /**
     * Sets this [Quaternion]'s components from the given axis vector and angle around the axis.
     *
     * @param x     double The x component of the axis.
     * @param y     double The y component of the axis.
     * @param z     double The z component of the axis.
     * @param angle double The rotation angle in degrees.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromAngleAxis(x: Double, y: Double, z: Double, angle: Double): Quaternion {
        return this.fromAngleAxis(Vector3(x, y, z), angle)
    }

    /**
     * Sets this [Quaternion]'s components from the given x, y anx z axis [Vector3]s.
     * The inputs must be ortho-normal.
     *
     * @param xAxis [Vector3] The x axis.
     * @param yAxis [Vector3] The y axis.
     * @param zAxis [Vector3] The z axis.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromAxes(@NonNull xAxis: Vector3, @NonNull yAxis: Vector3, @NonNull zAxis: Vector3): Quaternion {
        return fromAxes(xAxis.x, xAxis.y, xAxis.z, yAxis.x, yAxis.y, yAxis.z, zAxis.x, zAxis.y, zAxis.z)
    }

    /**
     * Sets this [Quaternion]'s components from the give x, y and z axis vectors
     * which must be ortho-normal.
     *
     * This method taken from libGDX, which took it from the following:
     *
     *
     *
     * Taken from Bones framework for JPCT, see http://www.aptalkarga.com/bones/ which in turn took it from Graphics
     * Gem code at
     * ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z.
     *
     *
     * @param xx double The x axis x coordinate.
     * @param xy double The x axis y coordinate.
     * @param xz double The x axis z coordinate.
     * @param yx double The y axis x coordinate.
     * @param yy double The y axis y coordinate.
     * @param yz double The y axis z coordinate.
     * @param zx double The z axis x coordinate.
     * @param zy double The z axis y coordinate.
     * @param zz double The z axis z coordniate.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromAxes(xx: Double, xy: Double, xz: Double, yx: Double, yy: Double, yz: Double,
                 zx: Double, zy: Double, zz: Double): Quaternion {
        // The trace is the sum of the diagonal elements; see
        // http://mathworld.wolfram.com/MatrixTrace.html
        val t = xx + yy + zz

        //Protect the division by s by ensuring that s >= 1
        val x: Double
        val y: Double
        val z: Double
        val w: Double
        if (t >= 0) {
            var s = sqrt(t + 1) // |s| >= 1
            w = 0.5 * s // |w| >= 0.5
            s = 0.5 / s //<- This division cannot be bad
            x = (zy - yz) * s
            y = (xz - zx) * s
            z = (yx - xy) * s
        } else if (xx > yy && xx > zz) {
            var s = sqrt(1.0 + xx - yy - zz) // |s| >= 1
            x = s * 0.5 // |x| >= 0.5
            s = 0.5 / s
            y = (yx + xy) * s
            z = (xz + zx) * s
            w = (zy - yz) * s
        } else if (yy > zz) {
            var s = sqrt(1.0 + yy - xx - zz) // |s| >= 1
            y = s * 0.5 // |y| >= 0.5
            s = 0.5 / s
            x = (yx + xy) * s
            z = (zy + yz) * s
            w = (xz - zx) * s
        } else {
            var s = sqrt(1.0 + zz - xx - yy) // |s| >= 1
            z = s * 0.5 // |z| >= 0.5
            s = 0.5 / s
            x = (xz + zx) * s
            y = (zy + yz) * s
            w = (yx - xy) * s
        }
        return setAll(w, x, y, z)
    }

    /**
     * Sets this [Quaternion]'s components from the given matrix.
     *
     * @param matrix [Matrix4] The rotation matrix.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromMatrix(@NonNull matrix: Matrix4): Quaternion {
        val value = DoubleArray(16)
        matrix.toArray(value)
        fromAxes(value[Matrix4.M00], value[Matrix4.M10], value[Matrix4.M20],
                value[Matrix4.M01], value[Matrix4.M11], value[Matrix4.M21],
                value[Matrix4.M02], value[Matrix4.M12], value[Matrix4.M22])
        return this
    }

    /**
     * Sets this [Quaternion]'s components from the given matrix.
     *
     * @param matrix [Matrix4] The rotation matrix.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromMatrix(@NonNull @Size(min = 16) matrix: DoubleArray): Quaternion {
        fromAxes(matrix[Matrix4.M00], matrix[Matrix4.M10], matrix[Matrix4.M20],
                matrix[Matrix4.M01], matrix[Matrix4.M11], matrix[Matrix4.M21],
                matrix[Matrix4.M02], matrix[Matrix4.M12], matrix[Matrix4.M22])
        return this
    }

    /**
     * Sets this [Quaternion] from the given Euler angles.
     *
     * @param yawIn   double The yaw angle in degrees.
     * @param pitch double The pitch angle in degrees.
     * @param roll  double The roll angle in degrees.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    @NonNull
    fun fromEuler(yawIn: Double, pitchIn: Double, rollIn: Double): Quaternion {
        var yaw = yawIn
        var pitch = pitchIn
        var roll = rollIn
        yaw = Math.toRadians(yaw)
        pitch = Math.toRadians(pitch)
        roll = Math.toRadians(roll)
        val hr = roll * 0.5
        val shr = sin(hr)
        val chr = cos(hr)
        val hp = pitch * 0.5
        val shp = sin(hp)
        val chp = cos(hp)
        val hy = yaw * 0.5
        val shy = sin(hy)
        val chy = cos(hy)
        val chy_shp = chy * shp
        val shy_chp = shy * chp
        val chy_chp = chy * chp
        val shy_shp = shy * shp

        x = chy_shp * chr + shy_chp * shr
        y = shy_chp * chr - chy_shp * shr
        z = chy_chp * shr - shy_shp * chr
        w = chy_chp * chr + shy_shp * shr
        return this
    }

    /**
     * Set this [Quaternion]'s components to the rotation between the given
     * two [Vector3]s. This will fail if the two vectors are parallel.
     *
     * @param u [Vector3] The base vector, should be normalized.
     * @param v [Vector3] The target vector, should be normalized.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromRotationBetween(@NonNull u: Vector3, @NonNull v: Vector3): Quaternion {
        val dot = u.dot(v)
        val dotError = 1.0 - abs(MathUtil.clamp(dot, -1.0, 1.0))
        if (dotError <= PARALLEL_TOLERANCE) {
            // The look and up vectors are parallel/anti-parallel
            if (dot < 0) {
                // The look and up vectors are parallel but opposite direction
                mTmpVec3.crossAndSet(WorldParameters.RIGHT_AXIS, u)
                if (mTmpVec3.length() < 1e-6) {
                    // Vectors were co-linear, pick another
                    mTmpVec3.crossAndSet(WorldParameters.UP_AXIS, u)
                }
                mTmpVec3.normalize()
                return fromAngleAxis(mTmpVec3, 180.0)
            } else {
                // The look and up vectors are parallel in the same direction
                return identity()
            }
        }
        mTmpVec3.crossAndSet(u, v).normalize()
        x = mTmpVec3.x
        y = mTmpVec3.y
        z = mTmpVec3.z
        w = 1 + dot
        normalize()
        return this
    }

    /**
     * Sets this [Quaternion]'s components to the rotation between the given
     * two vectors. The incoming vectors should be normalized. This will fail if the two
     * vectors are parallel.
     *
     * @param x1 double The base vector's x component.
     * @param y1 double The base vector's y component.
     * @param z1 double The base vector's z component.
     * @param x2 double The target vector's x component.
     * @param y2 double The target vector's y component.
     * @param z2 double The target vector's z component.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun fromRotationBetween(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Quaternion {
        mTmpVec1.setAll(x1, y1, z1).normalize()
        mTmpVec2.setAll(x2, y2, z2).normalize()
        return fromRotationBetween(mTmpVec1, mTmpVec2)
    }

    /**
     * Adds the provided [Quaternion] to this one. this = this + quat.
     *
     * @param quat [Quaternion] to be added to this one.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun add(@NonNull quat: Quaternion): Quaternion {
        w += quat.w
        x += quat.x
        y += quat.y
        z += quat.z
        return this
    }

    /**
     * Subtracts the provided [Quaternion] from this one. this = this - quat.
     *
     * @param quat [Quaternion] to be subtracted from this one.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun subtract(@NonNull quat: Quaternion): Quaternion {
        w -= quat.w
        x -= quat.x
        y -= quat.y
        z -= quat.z
        return this
    }

    /**
     * Multiplies each component of this [Quaternion] by the input
     * value.
     *
     * @param scalar double The value to multiply by.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun multiply(scalar: Double): Quaternion {
        w *= scalar
        x *= scalar
        y *= scalar
        z *= scalar
        return this
    }

    /**
     * Multiplies this [Quaternion] with another one. this = this * quat.
     *
     * @param quat [Quaternion] The other [Quaternion].
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun multiply(@NonNull quat: Quaternion): Quaternion {
        val tW = w
        val tX = x
        val tY = y
        val tZ = z

        w = tW * quat.w - tX * quat.x - tY * quat.y - tZ * quat.z
        x = tW * quat.x + tX * quat.w + tY * quat.z - tZ * quat.y
        y = tW * quat.y + tY * quat.w + tZ * quat.x - tX * quat.z
        z = tW * quat.z + tZ * quat.w + tX * quat.y - tY * quat.x
        return this
    }

    /**
     * Multiplies this [Quaternion] by a [Vector3].
     * Note that if you plan on using the returned [Vector3],
     * you should clone it immediately as it is an internal scratch
     * member of this [Quaternion] and may be modified at any
     * time. This is the same as out = q*vector*`q, meaning the magnitude
     * will be maintained.
     *
     * @param vector [Vector3] to multiply by.
     *
     * @return [Vector3] The result of the multiplication.
     */
    @NonNull
    fun multiply(@NonNull vector: Vector3): Vector3 {
        mTmpVec3.setAll(x, y, z)
        mTmpVec1.crossAndSet(mTmpVec3, vector)
        mTmpVec2.crossAndSet(mTmpVec3, mTmpVec1)
        mTmpVec1.multiply(2.0 * w)
        mTmpVec2.multiply(2.0)

        mTmpVec1.add(mTmpVec2)
        mTmpVec1.add(vector)

        return mTmpVec1
    }

    /**
     * Multiplies this [Quaternion] with another. this = quat * this.
     *
     * @param quat [Quaternion] The other [Quaternion].
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun multiplyLeft(@NonNull quat: Quaternion): Quaternion {
        val newW = quat.w * w - quat.x * x - quat.y * y - quat.z * z
        val newX = quat.w * x + quat.x * w + quat.y * z - quat.z * y
        val newY = quat.w * y + quat.y * w + quat.z * x - quat.x * z
        val newZ = quat.w * z + quat.z * w + quat.x * y - quat.y * x
        return setAll(newW, newX, newY, newZ)
    }

    /**
     * Normalizes this [Quaternion] to unit length.
     *
     * @return double The scaling factor used to normalize this [Quaternion].
     */
    fun normalize(): Double {
        val len = length2()
        if (len != 0.0 && abs(len - 1.0) > NORMALIZATION_TOLERANCE) {
            val factor = 1.0 / sqrt(len)
            multiply(factor)
        }
        return len
    }

    /**
     * Conjugate this [Quaternion].
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun conjugate(): Quaternion {
        x = -x
        y = -y
        z = -z
        return this
    }

    /**
     * Set this [Quaternion] to the normalized inverse of itself.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun inverse(): Quaternion {
        val norm = length2()
        val invNorm = 1.0 / norm
        return setAll(w * invNorm, -x * invNorm, -y * invNorm, -z * invNorm)
    }

    /**
     * Create a new [Quaternion] set to the normalized inverse of this one.
     *
     * @return [Quaternion] The new inverted [Quaternion].
     */
    @NonNull
    fun invertAndCreate(): Quaternion {
        val norm = length2()
        val invNorm = 1.0 / norm
        return Quaternion(w * invNorm, -x * invNorm, -y * invNorm, -z * invNorm)
    }

    /**
     * Computes and sets w on this [Quaternion] based on x,y,z components such
     * that this [Quaternion] is of unit length, if possible.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun computeW(): Quaternion {
        val t = 1.0 - x * x - y * y - z * z
        if (t < 0.0) {
            w = 0.0
        } else {
            // TODO: Are we sure this should be negative?
            w = -sqrt(t)
        }
        return this
    }

    /**
     * Creates a [Vector3] which represents the specified axis of this [Quaternion].
     *
     * @param axis {@Link Axis} The axis of this [Quaternion] to be returned.
     *
     * @return [Vector3] The z axis of this [Quaternion].
     */
    @NonNull
    fun getAxis(@NonNull axis: Axis): Vector3 {
        return when {
            axis === Axis.X -> xAxis
            axis === Axis.Y -> yAxis
            else -> zAxis
        }
    }

    /**
     * Calculates the Euclidean length of this [Quaternion].
     *
     * @return double The Euclidean length.
     */
    fun length(): Double {
        return sqrt(length2())
    }

    /**
     * Calculates the square of the Euclidean length of this [Quaternion].
     *
     * @return double The square of the Euclidean length.
     */
    fun length2(): Double {
        return w * w + x * x + y * y + z * z
    }

    /**
     * Calculates the dot product between this and another [Quaternion].
     *
     * @return double The dot product.
     */
    fun dot(@NonNull other: Quaternion): Double {
        return w * other.w + x * other.x + y * other.y + z * other.z
    }

    /**
     * Sets this [Quaternion] to an identity.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun identity(): Quaternion {
        w = 1.0
        x = 0.0
        y = 0.0
        z = 0.0
        return this
    }

    /**
     * Sets this [Quaternion] to the value of q = e^this.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun exp(): Quaternion {
        val vNorm = sqrt(x * x + y * y + z * z)
        val sin = sin(vNorm)
        w = cos(vNorm)
        // TODO: Do we really need the epsilon check? What if it fails?
        //if (Math.abs(sin) >= F_EPSILON) {
        val coeff = sin / vNorm
        x = coeff * x
        y = coeff * y
        z = coeff * z
        //}
        return this
    }

    /**
     * Creates a new [Quaternion] q initialized to the value of q = e^this.
     *
     * @return [Quaternion] The new [Quaternion] set to e^this.
     */
    @NonNull
    fun expAndCreate(): Quaternion {
        val result = Quaternion(this)
        result.exp()
        return result
    }

    /**
     * Sets this [Quaternion] to the value of q = log(this).
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun log(): Quaternion {
        val qNorm = length()
        if (qNorm > 0) {
            val vNorm = sqrt(x * x + y * y + z * z)
            val coeff = acos(w / qNorm) / vNorm
            w = ln(qNorm)
            x = coeff * x
            y = coeff * y
            z = coeff * z
        }
        return this
    }

    /**
     * Creates a new [Quaternion] q initialized to the value of q = log(this).
     *
     * @return [Quaternion] The new [Quaternion] set to log(q).
     */
    @NonNull
    fun logAndCreate(): Quaternion {
        val result = Quaternion(this)
        result.log()
        return result
    }

    /**
     * Sets this [Quaternion] to the value of q = this^p.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun pow(p: Double): Quaternion {
        val l = length()
        normalize()
        return log().multiply(p).exp().multiply(l.pow(p))
    }

    /**
     * Sets this [Quaternion] to the value of q = this^p.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun pow(@NonNull p: Quaternion): Quaternion {
        return log().multiply(p).exp()
    }

    /**
     * Creates a new [Quaternion] q initialized to the value of q = this^p.
     *
     * @return [Quaternion] The new [Quaternion].
     */
    @NonNull
    fun powAndCreate(p: Double): Quaternion {
        return Quaternion(this).pow(p)
    }

    /**
     * Creates a new [Quaternion] q initialized to the value of q = this^p.
     *
     * @return [Quaternion] The new [Quaternion].
     */
    @NonNull
    fun powAndCreate(@NonNull p: Quaternion): Quaternion {
        return Quaternion(this).pow(p)
    }

    /**
     * Performs spherical linear interpolation between this and the provided [Quaternion]
     * and sets this [Quaternion] to the normalized result.
     *
     * @param end [Quaternion] The destination point.
     * @param t   double The interpolation value. [0-1] Where 0 represents this and 1 represents end.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun slerp(@NonNull end: Quaternion, @FloatRange(from = 0.0, to = 1.0) t: Double): Quaternion {
        return slerp(this, end, t, true)
    }

    /**
     * Performs spherical linear interpolation between the provided [Quaternion]s and
     * sets this [Quaternion] to the normalized result.
     *
     * @param q1 [Quaternion] The starting point.
     * @param q2 [Quaternion] The destination point.
     * @param t  double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun slerp(@NonNull q1: Quaternion, @NonNull q2: Quaternion, @FloatRange(from = 0.0, to = 1.0) t: Double): Quaternion {
        return slerp(q1, q2, t, true)
    }

    /**
     * Performs spherical linear interpolation between the provided [Quaternion]s and
     * sets this [Quaternion] to the normalized result.
     *
     * @param start        [Quaternion] The starting point.
     * @param end          [Quaternion] The destination point.
     * @param t            `double` The interpolation value. [0-1] Where 0 represents start and 1 represents end.
     * @param shortestPath `boolean` always return the shortest path.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun slerp(@NonNull start: Quaternion, @NonNull end: Quaternion, @FloatRange(from = 0.0, to = 1.0) t: Double,
              shortestPath: Boolean): Quaternion {
        // Check for equality and skip operation.
        if (equals(end)) {
            return this
        }

        var result = start.dot(end)

        if (shortestPath && result < 0.0f) {
            end.x = -end.x
            end.y = -end.y
            end.z = -end.z
            end.w = -end.w
            result = -result
        }

        var scale0 = 1 - t
        var scale1 = t

        if (!shortestPath || 1 - result > 0.1) {
            val theta = acos(result)
            val invSinTheta = 1 / sin(theta)

            scale0 = sin((1 - t) * theta) * invSinTheta
            scale1 = sin(t * theta) * invSinTheta
        }

        x = scale0 * start.x + scale1 * end.x
        y = scale0 * start.y + scale1 * end.y
        z = scale0 * start.z + scale1 * end.z
        w = scale0 * start.w + scale1 * end.w
        normalize()
        return this
    }

    /**
     * Creates a [Matrix4] representing this [Quaternion].
     *
     * @return [Matrix4] representing this [Quaternion].
     */
    @NonNull
    fun toRotationMatrix(): Matrix4 {
        val matrix = Matrix4()
        toRotationMatrix(matrix)
        return matrix
    }

    /**
     * Sets the provided [Matrix4] to represent this [Quaternion]. This [Quaternion] must be
     * normalized.
     */
    @NonNull
    fun toRotationMatrix(@NonNull matrix: Matrix4): Matrix4 {
        toRotationMatrix(matrix.doubleValues)
        return matrix
    }

    /**
     * Sets the provided double[] to be a 4x4 rotation matrix representing this [Quaternion]. This
     * [Quaternion] must be normalized.
     *
     * @param matrix double[] representing a 4x4 rotation matrix in column major order.
     */
    fun toRotationMatrix(@NonNull @Size(min = 16) matrix: DoubleArray) {
        val x2 = x * x
        val y2 = y * y
        val z2 = z * z
        val xy = x * y
        val xz = x * z
        val yz = y * z
        val wx = w * x
        val wy = w * y
        val wz = w * z

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

        matrix[Matrix4.M03] = 0.0
        matrix[Matrix4.M13] = 0.0
        matrix[Matrix4.M23] = 0.0
        matrix[Matrix4.M33] = 1.0
    }

    /**
     * Sets this [Quaternion] to be oriented to a target [Vector3].
     * It is safe to use the input vectors for other things as they are cloned internally.
     *
     * @param lookAt      [Vector3] The point to look at.
     * @param upDirection [Vector3] to use as the up direction.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @NonNull
    fun lookAt(@NonNull lookAt: Vector3, @NonNull upDirection: Vector3): Quaternion {
        mTmpVec1.setAll(lookAt)
        mTmpVec2.setAll(upDirection)
        // Vectors are parallel/anti-parallel if their dot product magnitude and length product are equal
        val dotProduct = Vector3.dot(lookAt, upDirection)
        val dotError = abs(abs(dotProduct) - lookAt.length() * upDirection.length())
        if (dotError <= PARALLEL_TOLERANCE) {
            // The look and up vectors are parallel
            mTmpVec2.normalize()
            if (dotProduct < 0) {
                mTmpVec1.inverse()
            }
            fromRotationBetween(WorldParameters.FORWARD_AXIS, mTmpVec1)
            return this
        }
        Vector3.orthoNormalize(mTmpVec1, mTmpVec2) // Find the forward and up vectors
        mTmpVec3.crossAndSet(mTmpVec2, mTmpVec1) // Create the right vector
        fromAxes(mTmpVec3, mTmpVec2, mTmpVec1)
        return this
    }

    /**
     * Measures the angle in radians between this [Quaternion] and another.
     *
     * @param other [Quaternion] The other [Quaternion].
     */
    fun angleBetween(@NonNull other: Quaternion): Double {
        val inv = clone().inverse()
        val res = inv.multiplyLeft(other)
        return 2.0 * acos(res.w)
    }

    /**
     * Clones this [Quaternion].
     *
     * @return [Quaternion] A copy of this [Quaternion].
     */
    @NonNull
    public override fun clone(): Quaternion {
        return Quaternion(w, x, y, z)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Quaternion) {
            return false
        }
        val comp = other as Quaternion?
        return x == comp!!.x && y == comp.y && z == comp.z && w == comp.w
    }

    /**
     * Compares this [Quaternion] to another with a tolerance.
     *
     * @param other     [Quaternion] The other [Quaternion].
     * @param tolerance double The tolerance for equality.
     *
     * @return boolean True if the two [Quaternion]s equate within the specified tolerance.
     */
    fun equals(@NonNull other: Quaternion, tolerance: Double): Boolean {
        val fCos = dot(other)
        if (fCos > 1.0 && fCos - 1.0 < tolerance) {
            return true
        }
        val angle = acos(fCos)
        return abs(angle) <= tolerance || MathUtil.realEqual(angle, MathUtil.PI, tolerance)
    }

    override fun toString(): String {
        val sb = StringBuffer()
        sb.append("Quaternion <w, x, y, z>: <")
                .append(w)
                .append(", ")
                .append(x)
                .append(", ")
                .append(y)
                .append(", ")
                .append(z)
                .append(">")
        return sb.toString()
    }

    companion object {
        //Tolerances
        //public static final double F_EPSILON               = .001;
        const val NORMALIZATION_TOLERANCE = 1e-6
        const val PARALLEL_TOLERANCE = 1e-6
        @NonNull
        private val sTmp1 = Quaternion(0.0, 0.0, 0.0, 0.0)
        @NonNull
        private val sTmp2 = Quaternion(0.0, 0.0, 0.0, 0.0)

        /**
         * Creates a new [Quaternion] and sets its components to the rotation between the given
         * two vectors. The incoming vectors should be normalized.
         *
         * @param u [Vector3] The source vector.
         * @param v [Vector3] The destination vector.
         *
         * @return [Quaternion] The new [Quaternion].
         */
        @NonNull
        fun createFromRotationBetween(@NonNull u: Vector3, @NonNull v: Vector3): Quaternion {
            val q = Quaternion()
            q.fromRotationBetween(u, v)
            return q
        }

        /**
         * Retrieves a new [Quaternion] initialized to identity.
         *
         * @return A new identity [Quaternion].
         */
        val identity: Quaternion
            @NonNull
            get() = Quaternion(1.0, 0.0, 0.0, 0.0)

        /**
         * Performs linear interpolation between two [Quaternion]s and creates a new one
         * for the result.
         *
         * @param start        [Quaternion] The starting point.
         * @param end          [Quaternion] The destination point.
         * @param t            double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
         * @param shortestPath boolean indicating if the shortest path should be used.
         *
         * @return [Quaternion] The interpolated [Quaternion].
         */
        @NonNull
        fun lerp(@NonNull start: Quaternion, @NonNull end: Quaternion,
                 @FloatRange(from = 0.0, to = 1.0) t: Double, shortestPath: Boolean): Quaternion {
            // Check for equality and skip operation.
            if (start == end) {
                return sTmp1.setAll(end)
            }
            // TODO: Thread safety issue here
            sTmp1.setAll(start)
            sTmp2.setAll(end)
            val fCos = sTmp1.dot(sTmp2)
            if (fCos < 0.0f && shortestPath) {
                sTmp2.inverse()
                sTmp2.subtract(sTmp1)
                sTmp2.multiply(t)
                sTmp1.add(sTmp2)
            } else {
                sTmp2.subtract(sTmp1)
                sTmp2.multiply(t)
                sTmp1.add(sTmp2)
            }
            return sTmp1
        }

        /**
         * Performs normalized linear interpolation between two [Quaternion]s and creates a new one
         * for the result.
         *
         * @param q1           [Quaternion] The starting point.
         * @param q2           [Quaternion] The destination point.
         * @param t            double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
         * @param shortestPath boolean indicating if the shortest path should be used.
         *
         * @return [Quaternion] The normalized interpolated [Quaternion].
         */
        @NonNull
        fun nlerp(@NonNull q1: Quaternion, @NonNull q2: Quaternion,
                  @FloatRange(from = 0.0, to = 1.0) t: Double, shortestPath: Boolean): Quaternion {
            val result = lerp(q1, q2, t, shortestPath)
            result.normalize()
            return result
        }

        /**
         * Creates a new [Quaternion] which is oriented to a target [Vector3].
         * It is safe to use the input vectors for other things as they are cloned internally.
         *
         * @param lookAt      [Vector3] The point to look at.
         * @param upDirection [Vector3] to use as the up direction.
         *
         * @return [Quaternion] The new [Quaternion] representing the requested orientation.
         */
        @NonNull
        fun lookAtAndCreate(@NonNull lookAt: Vector3, @NonNull upDirection: Vector3): Quaternion {
            val ret = Quaternion()
            return ret.lookAt(lookAt, upDirection)
        }
    }
}
