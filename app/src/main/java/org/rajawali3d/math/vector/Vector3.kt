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
@file:Suppress("LocalVariableName")

package org.rajawali3d.math.vector

import androidx.annotation.NonNull
import androidx.annotation.Size
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.Quaternion
import kotlin.math.*

/**
 * Encapsulates a 3D point/vector.
 *
 *
 * This class borrows heavily from the implementation.
 *
 * @author dennis.ippel
 * @author Jared Woolston (jwoolston@tenkiv.com)
 * @author Dominic Cerisano (Gram-Schmidt orthonormalization)
 * @see [libGDX->Vector3](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Vector3.java)
 *
 *
 * This class is not thread safe and must be confined to a single thread or protected by
 * some external locking mechanism if necessary. All static methods are thread safe.
 */
class Vector3 : Cloneable {
    //The vector components
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()
    var z: Double = 0.toDouble()

    // Scratch Vector3. We use lazy loading here to prevent memory explosion.
    private var mTmpVector3: Vector3? = null

    // Scratch Matrix4. We use lazy loading here to prevent memory explosion.
    private var mTmpMatrix4: Matrix4? = null

    /**
     * Checks if this [Vector3] is of unit length with a default
     * margin of error of 1e-8.
     *
     * @return boolean True if this [Vector3] is of unit length.
     */
    val isUnit: Boolean
        get() = isUnit(1e-8)

    /**
     * Checks if this [Vector3] is a zero vector.
     *
     * @return boolean True if all 3 components are equal to zero.
     */
    val isZero: Boolean
        get() = x == 0.0 && y == 0.0 && z == 0.0

    /**
     * Enumeration for the 3 component axes.
     */
    enum class Axis {
        X, Y, Z
    }

    /**
     * Constructs a new [Vector3] at (0, 0, 0).
     */
    constructor() {
        // They are technically zero, but we wont rely on the uninitialized state here.
        x = 0.0
        y = 0.0
        z = 0.0
    }

    /**
     * Constructs a new [Vector3] at <from></from>, from, from>,>.
     *
     * @param from double which all components will be initialized to.
     */
    constructor(from: Double) {
        x = from
        y = from
        z = from
    }

    /**
     * Constructs a new [Vector3] with components matching the input [Vector3].
     *
     * @param from [Vector3] to initialize the components with.
     */
    constructor(/* /* @NonNull */ */ from: Vector3) {
        x = from.x
        y = from.y
        z = from.z
    }

    /**
     * Constructs a new [Vector3] with components initialized from the input [String] array.
     *
     * @param values A [String] array of values to be parsed for each component.
     *
     * @throws IllegalArgumentException if there are fewer than 3 values in the array.
     * @throws NumberFormatException if there is a problem parsing the [String] values into doubles.
     */
    @Throws(IllegalArgumentException::class, NumberFormatException::class)
    constructor(/* /* @NonNull */ */ @Size(min = 3) values: Array<String>)
            : this(java.lang.Float.parseFloat(values[0]).toDouble(),
            java.lang.Float.parseFloat(values[1]).toDouble(),
            java.lang.Float.parseFloat(values[2]).toDouble())

    /**
     * Constructs a new [Vector3] with components initialized from the input double array.
     *
     * @param values A double array of values to be parsed for each component.
     *
     * @throws IllegalArgumentException if there are fewer than 3 values in the array.
     */
    @Throws(IllegalArgumentException::class)
    constructor(/* /* @NonNull */ */ @Size(min = 3) values: DoubleArray) {
        if (values.size < 3)
            throw IllegalArgumentException("Vector3 must be initialized with an array length of at least 3.")
        x = values[0]
        y = values[1]
        z = values[2]
    }

    /**
     * Constructs a new [Vector3] object with components initialized to the specified values.
     *
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     */
    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    /**
     * Sets all components of this [Vector3] to the specified values.
     *
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun setAll(x: Double, y: Double, z: Double): Vector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    /**
     * Sets all components of this [Vector3] to the values provided
     * by the input [Vector3].
     *
     * @param other [Vector3] The vector to copy.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun setAll(/* /* @NonNull */ */ other: Vector3): Vector3 {
        x = other.x
        y = other.y
        z = other.z
        return this
    }

    /**
     * Sets all components of this [Vector3] to the values provided representing
     * the input [Axis].
     *
     * @param axis [Axis] The cardinal axis to set the values to.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun setAll(/* /* @NonNull */ */ axis: Axis): Vector3 {
        return setAll(getAxisVector(axis))
    }

    /**
     * Adds the provided [Vector3] to this one.
     *
     * @param v [Vector3] to be added to this one.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun add(/* /* @NonNull */ */ v: Vector3): Vector3 {
        x += v.x
        y += v.y
        z += v.z
        return this
    }

    /**
     * Adds the given values to the respective components of this [Vector3].
     *
     * @param x The value to add to the x component.
     * @param y The value to add to the y component.
     * @param z The value to add to the z component.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun add(x: Double, y: Double, z: Double): Vector3 {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    /**
     * Adds the given value to each component of this [Vector3].
     *
     * @param value double value to add.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun add(value: Double): Vector3 {
        x += value
        y += value
        z += value
        return this
    }

    /**
     * Adds two input [Vector3] objects and sets this one to the result.
     *
     * @param u [Vector3] The first vector.
     * @param v [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun addAndSet(/* /* @NonNull */ */ u: Vector3, /* /* @NonNull */ */ v: Vector3): Vector3 {
        x = u.x + v.x
        y = u.y + v.y
        z = u.z + v.z
        return this
    }

    /**
     * Subtracts the provided [Vector3] from this one.
     *
     * @param v [Vector3] to be subtracted from this one.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun subtract(/* /* @NonNull */ */ v: Vector3): Vector3 {
        x -= v.x
        y -= v.y
        z -= v.z
        return this
    }

    /**
     * Subtracts the given values from the respective components of this [Vector3].
     *
     * @param x The value to subtract to the x component.
     * @param y The value to subtract to the y component.
     * @param z The value to subtract to the z component.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun subtract(x: Double, y: Double, z: Double): Vector3 {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    /**
     * Subtracts the given value from each component of this [Vector3].
     *
     * @param value double value to subtract.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun subtract(value: Double): Vector3 {
        x -= value
        y -= value
        z -= value
        return this
    }

    /**
     * Subtracts two input [Vector3] objects and sets this one to the result.
     *
     * @param u [Vector3] The first vector.
     * @param v [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun subtractAndSet(/* /* @NonNull */ */ u: Vector3, /* /* @NonNull */ */ v: Vector3): Vector3 {
        x = u.x - v.x
        y = u.y - v.y
        z = u.z - v.z
        return this
    }

    /**
     * Scales each component of this [Vector3] by the specified value.
     *
     * @param value double The value to scale each component by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun multiply(value: Double): Vector3 {
        x *= value
        y *= value
        z *= value
        return this
    }

    /**
     * Scales each component of this [Vector3] by the corresponding components
     * of the provided [Vector3].
     *
     * @param v [Vector3] containing the values to scale by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun multiply(/* /* @NonNull */ */ v: Vector3): Vector3 {
        x *= v.x
        y *= v.y
        z *= v.z
        return this
    }

    /**
     * Multiplies this [Vector3] and the provided 4x4 matrix.
     *
     * @param matrix double[16] representation of a 4x4 matrix.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun multiply(/* /* @NonNull */ */ @Size(min = 16) matrix: DoubleArray): Vector3 {
        val vx = x
        val vy = y
        val vz = z
        x = vx * matrix[Matrix4.M00] + vy * matrix[Matrix4.M01] + vz * matrix[Matrix4.M02] + matrix[Matrix4.M03]
        y = vx * matrix[Matrix4.M10] + vy * matrix[Matrix4.M11] + vz * matrix[Matrix4.M12] + matrix[Matrix4.M13]
        z = vx * matrix[Matrix4.M20] + vy * matrix[Matrix4.M21] + vz * matrix[Matrix4.M22] + matrix[Matrix4.M23]
        return this
    }

    /**
     * Multiplies this [Vector3] and the provided 4x4 matrix.
     *
     * @param matrix [Matrix4] to multiply this [Vector3] by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun multiply(/* /* @NonNull */ */ matrix: Matrix4): Vector3 {
        return multiply(matrix.doubleValues)
    }

    /**
     * Multiplies two input [Vector3] objects and sets this one to the result.
     *
     * @param u [Vector3] The first vector.
     * @param v [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun multiplyAndSet(/* /* @NonNull */ */ u: Vector3, /* /* @NonNull */ */ v: Vector3): Vector3 {
        x = u.x * v.x
        y = u.y * v.y
        z = u.z * v.z
        return this
    }

    /**
     * Divide each component of this [Vector3] by the specified value.
     *
     * @param value double The value to divide each component by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun divide(value: Double): Vector3 {
        x /= value
        y /= value
        z /= value
        return this
    }

    /**
     * Divides each component of this [Vector3] by the corresponding components
     * of the provided [Vector3].
     *
     * @param v [Vector3] containing the values to divide by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* /* @NonNull */ */
    fun divide(/* @NonNull */ v: Vector3): Vector3 {
        x /= v.x
        y /= v.y
        z /= v.z
        return this
    }

    /**
     * Divides two input [Vector3] objects and sets this one to the result.
     *
     * @param u [Vector3] The first vector.
     * @param v [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun divideAndSet(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
        x = u.x / v.x
        y = u.y / v.y
        z = u.z / v.z
        return this
    }

    /**
     * Scales an input [Vector3] by a value and sets this one to the result.
     *
     * @param v [Vector3] The [Vector3] to scale.
     * @param b [Vector3] The scaling factor.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun scaleAndSet(/* @NonNull */ v: Vector3, b: Double): Vector3 {
        x = v.x * b
        y = v.y * b
        z = v.z * b
        return this
    }

    /**
     * Rotates this [Vector3] as specified by the provided [Quaternion].
     *
     * @param quaternion [Quaternion] describing the rotation.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun rotateBy(/* @NonNull */ quaternion: Quaternion): Vector3 {
        return setAll(quaternion.multiply(this))
    }

    /**
     * Sets this [Vector3] to a rotation about the X axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun rotateX(angle: Double): Vector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (mTmpVector3 == null) {
            mTmpVector3 = Vector3(this)
        } else {
            mTmpVector3!!.setAll(x, y, z)
        }
        y = mTmpVector3!!.y * cosRY - mTmpVector3!!.z * sinRY
        z = mTmpVector3!!.y * sinRY + mTmpVector3!!.z * cosRY
        return this
    }

    /**
     * Sets this [Vector3] to a rotation about the Y axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun rotateY(angle: Double): Vector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (mTmpVector3 == null) {
            mTmpVector3 = Vector3(this)
        } else {
            mTmpVector3!!.setAll(x, y, z)
        }
        x = mTmpVector3!!.x * cosRY + mTmpVector3!!.z * sinRY
        z = mTmpVector3!!.x * -sinRY + mTmpVector3!!.z * cosRY
        return this
    }

    /**
     * Sets this [Vector3] to a rotation about the Z axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun rotateZ(angle: Double): Vector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (mTmpVector3 == null) {
            mTmpVector3 = Vector3(this)
        } else {
            mTmpVector3!!.setAll(x, y, z)
        }
        x = mTmpVector3!!.x * cosRY - mTmpVector3!!.y * sinRY
        y = mTmpVector3!!.x * sinRY + mTmpVector3!!.y * cosRY
        return this
    }

    /**
     * Normalize this [Vector3] to unit length.
     *
     * @return double The initial magnitude.
     */
    fun normalize(): Double {
        val mag = sqrt(x * x + y * y + z * z)
        if (mag != 0.0 && mag != 1.0) {
            val mod = 1 / mag
            x *= mod
            y *= mod
            z *= mod
        }
        return mag
    }

    /**
     * Inverts the direction of this [Vector3].
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun inverse(): Vector3 {
        x = -x
        y = -y
        z = -z
        return this
    }

    /**
     * Inverts the direction of this [Vector3] and creates a new one set with the result.
     *
     * @return [Vector3] The resulting [Vector3].
     */
    /* @NonNull */
    fun invertAndCreate(): Vector3 {
        return Vector3(-x, -y, -z)
    }

    /**
     * Computes the Euclidean length of this [Vector3];
     *
     * @return double The Euclidean length.
     */
    fun length(): Double {
        return length(this)
    }

    /**
     * Computes the squared Euclidean length of this [Vector3];
     *
     * @return double The squared Euclidean length.
     */
    fun length2(): Double {
        return x * x + y * y + z * z
    }

    /**
     * Computes the Euclidean length of this [Vector3] to the specified [Vector3].
     *
     * @param v [Vector3] The [Vector3] to compute the distance to.
     *
     * @return double The Euclidean distance.
     */
    fun distanceTo(/* @NonNull */ v: Vector3): Double {
        val a = x - v.x
        val b = y - v.y
        val c = z - v.z
        return sqrt(a * a + b * b + c * c)
    }

    /**
     * Computes the Euclidean length of this [Vector3] to the specified point.
     *
     * @param x double The point x coordinate.
     * @param y double The point y coordinate.
     * @param z double The point z coordinate.
     *
     * @return double The Euclidean distance.
     */
    fun distanceTo(x: Double, y: Double, z: Double): Double {
        val a = this.x - x
        val b = this.y - y
        val c = this.z - z
        return sqrt(a * a + b * b + c * c)
    }

    /**
     * Computes the squared Euclidean length of this [Vector3] to the specified [Vector3].
     *
     * @param v [Vector3] The [Vector3] to compute the distance to.
     *
     * @return double The squared Euclidean distance.
     */
    fun distanceTo2(/* @NonNull */ v: Vector3): Double {
        val a = x - v.x
        val b = y - v.y
        val c = z - v.z
        return a * a + b * b + c * c
    }

    /**
     * Computes the squared Euclidean length of this [Vector3] to the specified point.
     *
     * @param x double The point x coordinate.
     * @param y double The point y coordinate.
     * @param z double The point z coordinate.
     *
     * @return double The squared Euclidean distance.
     */
    fun distanceTo2(x: Double, y: Double, z: Double): Double {
        val a = this.x - x
        val b = this.y - y
        val c = this.z - z
        return a * a + b * b + c * c
    }

    /**
     * Sets this [Vector3] to the absolute value of itself.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun absoluteValue(): Vector3 {
        x = abs(x)
        y = abs(y)
        z = abs(z)
        return this
    }

    /**
     * Projects the specified [Vector3] onto this one and sets this [Vector3]
     * to the result.
     *
     * @param v [Vector3] to be projected.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun project(/* @NonNull */ v: Vector3): Vector3 {
        val d = dot(v)
        val d_div = d / length2()
        return multiply(d_div)
    }

    /**
     * Multiplies this [Vector3] by the provided 4x4 matrix and divides by w.
     * Typically this is used for project/un-project of a [Vector3].
     *
     * @param matrix double[16] array representation of a 4x4 matrix to project with.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun project(/* @NonNull */ @Size(min = 16) matrix: DoubleArray): Vector3 {
        if (mTmpMatrix4 == null) {
            mTmpMatrix4 = Matrix4(matrix)
        } else {
            mTmpMatrix4!!.setAll(matrix)
        }
        return project(mTmpMatrix4!!)
    }

    /**
     * Multiplies this [Vector3] by the provided [Matrix4] and divides by w.
     * Typically this is used for project/un-project of a [Vector3].
     *
     * @param matrix [Matrix4] 4x4 matrix to project with.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun project(/* @NonNull */ matrix: Matrix4): Vector3 {
        return matrix.projectVector(this)
    }

    /**
     * Calculates the angle between this [Vector3] and the provided [Vector3].
     *
     * @param v [Vector3] The [Vector3] The [Vector3] to calculate the angle to.
     *
     * @return `double` The calculated angle, in degrees.
     */
    fun angle(/* @NonNull */ v: Vector3): Double {
        var dot = dot(v)
        dot /= length() * v.length()
        return Math.toDegrees(acos(dot))
    }

    /**
     * Computes the vector dot product between this [Vector3] and the specified [Vector3].
     *
     * @param v [Vector3] to compute the dot product with.
     *
     * @return double The dot product.
     */
    fun dot(/* @NonNull */ v: Vector3): Double {
        return x * v.x + y * v.y + z * v.z
    }

    /**
     * Computes the vector dot product between this [Vector3] and the specified vector.
     *
     * @param x double The x component of the specified vector.
     * @param y double The y component of the specified vector.
     * @param z double The z component of the specified vector.
     *
     * @return double The dot product.
     */
    fun dot(x: Double, y: Double, z: Double): Double {
        return this.x * x + this.y * y + this.z * z
    }

    /**
     * Computes the cross product between this [Vector3] and the specified [Vector3],
     * setting this to the result.
     *
     * @param v [Vector3] the other [Vector3] to cross with.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun cross(/* @NonNull */ v: Vector3): Vector3 {
        if (mTmpVector3 == null) {
            mTmpVector3 = Vector3(this)
        } else {
            mTmpVector3!!.setAll(this)
        }
        x = mTmpVector3!!.y * v.z - mTmpVector3!!.z * v.y
        y = mTmpVector3!!.z * v.x - mTmpVector3!!.x * v.z
        z = mTmpVector3!!.x * v.y - mTmpVector3!!.y * v.x
        return this
    }

    /**
     * Computes the cross product between this [Vector3] and the specified vector,
     * setting this to the result.
     *
     * @param x double The x component of the other vector.
     * @param y double The y component of the other vector.
     * @param z double The z component of the other vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun cross(x: Double, y: Double, z: Double): Vector3 {
        if (mTmpVector3 == null) {
            mTmpVector3 = Vector3(this)
        } else {
            mTmpVector3!!.setAll(this)
        }
        this.x = mTmpVector3!!.y * z - mTmpVector3!!.z * y
        this.y = mTmpVector3!!.z * x - mTmpVector3!!.x * z
        this.z = mTmpVector3!!.x * y - mTmpVector3!!.y * x
        return this
    }

    /**
     * Computes the cross product between two [Vector3] objects and and sets
     * a this to the result.
     *
     * @param u [Vector3] The first [Vector3] to cross.
     * @param v [Vector3] The second [Vector3] to cross.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun crossAndSet(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
        return setAll(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x)
    }

    /**
     * Creates a [Quaternion] which represents the rotation from a this [Vector3]
     * to the provided [Vector3]. Adapted from OGRE 3D engine.
     *
     * @param direction [Vector3] The direction to rotate to.
     *
     * @return [Quaternion] The [Quaternion] representing the rotation.
     */
    /* @NonNull */
    fun getRotationTo(/* @NonNull */ direction: Vector3): Quaternion {
        return Quaternion.createFromRotationBetween(this, direction)
    }

    /**
     * Performs a linear interpolation between this [Vector3] and to by the specified amount.
     * The result will be stored in the current object which means that the current
     * x, y, z values will be overridden.
     *
     * @param target     [Vector3] Ending point.
     * @param t double [0-1] interpolation value.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun lerp(/* @NonNull */ target: Vector3, t: Double): Vector3 {
        return multiply(1.0 - t).add(target.x * t, target.y * t, target.z * t)
    }

    /**
     * Performs a linear interpolation between from and to by the specified amount.
     * The result will be stored in the current object which means that the current
     * x, y, z values will be overridden.
     *
     * @param from   [Vector3] Starting point.
     * @param to     [Vector3] Ending point.
     * @param amount double [0-1] interpolation value.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    /* @NonNull */
    fun lerpAndSet(/* @NonNull */ from: Vector3, /* @NonNull */ to: Vector3, amount: Double): Vector3 {
        x = from.x + (to.x - from.x) * amount
        y = from.y + (to.y - from.y) * amount
        z = from.z + (to.z - from.z) * amount
        return this
    }

    /**
     * Clones this [Vector3].
     *
     * @return [Vector3] A copy of this [Vector3].
     */
    /* @NonNull */
    public override fun clone(): Vector3 {
        return Vector3(x, y, z)
    }

    /**
     * Checks if this [Vector3] is of unit length with a specified
     * margin of error.
     *
     * @param margin double The desired margin of error for the test.
     *
     * @return boolean True if this [Vector3] is of unit length.
     */
    fun isUnit(margin: Double): Boolean {
        return abs(length2() - 1) < margin * margin
    }

    /**
     * Checks if the length of this [Vector3] is smaller than the specified margin.
     *
     * @param margin double The desired margin of error for the test.
     *
     * @return boolean True if this [Vector3]'s length is smaller than the margin specified.
     */
    fun isZero(margin: Double): Boolean {
        return length2() <= margin * margin
    }

    /**
     * Does a component by component comparison of this [Vector3] and the specified [Vector3]
     * and returns the result.
     *
     * @param o [Object] to compare with this one.
     *
     * @return boolean True if this [Vector3]'s components match with the components of the input.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val vector3 = other as Vector3?
        return (vector3!!.x == x
                && vector3.y == y
                && vector3.z == z)
    }

    /**
     * Does a component by component comparison of this [Vector3] and the specified [Vector3]
     * with an error parameter and returns the result.
     *
     * @param obj [Vector3] to compare with this one.
     * @param error `double` The maximum allowable difference to be considered equal.
     *
     * @return boolean True if this [Vector3]'s components match with the components of the input.
     */
    fun equals(/* @NonNull */ obj: Vector3, error: Double): Boolean {
        return abs(obj.x - x) <= error && abs(obj.y - y) <= error && abs(obj.z - z) <= error
    }

    /**
     * Fills x, y, z values into first three positions in the
     * supplied array, if it is large enough to accommodate
     * them.
     *
     * @param array The array to be populated
     *
     * @return The passed array with the xyz values inserted
     */
    /* @NonNull */
    @Size(min = 3)
    fun toArray(@Size(min = 3) array: DoubleArray?): DoubleArray? {
        if (array != null && array.size >= 3) {
            array[0] = x
            array[1] = y
            array[2] = z
        }

        return array
    }

    /**
     * Returns an array representation of this Vector3.
     *
     * @return An array containing this Vector3's xyz values.
     */
    /* @NonNull */
    @Size(3)
    fun toArray(): DoubleArray? {
        return toArray(DoubleArray(3))
    }

    /* @NonNull */
    override fun toString(): String {
        val sb = StringBuffer()
        sb.append("Vector3 <x, y, z>: <")
                .append(x)
                .append(", ")
                .append(y)
                .append(", ")
                .append(z)
                .append(">")
        return sb.toString()
    }

    companion object {

        //Unit vectors oriented to each axis
        //DO NOT EVER MODIFY THE VALUES OF THESE MEMBERS
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val X = Vector3(1.0, 0.0, 0.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val Y = Vector3(0.0, 1.0, 0.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val Z = Vector3(0.0, 0.0, 1.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val NEG_X = Vector3(-1.0, 0.0, 0.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val NEG_Y = Vector3(0.0, -1.0, 0.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val NEG_Z = Vector3(0.0, 0.0, -1.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val ZERO = Vector3(0.0, 0.0, 0.0)
        /**
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        /* @NonNull */
        val ONE = Vector3(1.0, 1.0, 1.0)

        /**
         * Adds two input [Vector3] objects and creates a new one to hold the result.
         *
         * @param u [Vector3] The first vector.
         * @param v [Vector3] The second vector.
         *
         * @return [Vector3] The resulting [Vector3].
         */
        /* @NonNull */
        fun addAndCreate(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
            return Vector3(u.x + v.x, u.y + v.y, u.z + v.z)
        }

        /**
         * Subtracts two input [Vector3] objects and creates u new one to hold the result.
         *
         * @param u [Vector3] The first vector.
         * @param v [Vector3] The second vector
         *
         * @return [Vector3] The resulting [Vector3].
         */
        /* @NonNull */
        fun subtractAndCreate(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
            return Vector3(u.x - v.x, u.y - v.y, u.z - v.z)
        }

        /**
         * Multiplies two input [Vector3] objects and creates a new one to hold the result.
         *
         * @param u [Vector3] The first vector.
         * @param v [Vector3] The second vector
         *
         * @return [Vector3] The resulting [Vector3].
         */
        /* @NonNull */
        fun multiplyAndCreate(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
            return Vector3(u.x * v.x, u.y * v.y, u.z * v.z)
        }

        /**
         * Scales each component of this [Vector3] by the specified value and creates a new one to hold the result.
         *
         * @param v     [Vector3] The first vector.
         * @param value double The value to scale each component by.
         *
         * @return [Vector3] The resulting [Vector3].
         */
        /* @NonNull */
        fun multiplyAndCreate(/* @NonNull */ v: Vector3, value: Double): Vector3 {
            return Vector3(v.x * value, v.y * value, v.z * value)
        }

        /**
         * Scales an input [Vector3] by a value and creates a new one to hold the result.
         *
         * @param u [Vector3] The [Vector3] to scale.
         * @param v [Vector3] The scaling factor.
         *
         * @return [Vector3] The resulting [Vector3].
         */
        /* @NonNull */
        fun scaleAndCreate(/* @NonNull */ u: Vector3, v: Double): Vector3 {
            return Vector3(u.x * v, u.y * v, u.z * v)
        }

        /**
         * Applies Gram-Schmitt Ortho-normalization to the given set of input [Vector3] objects.
         *
         * @param vecs Array of [Vector3] objects to be ortho-normalized.
         */
        fun orthoNormalize(/* @NonNull */ @Size(min = 2) vecs: Array<Vector3>) {
            for (i in vecs.indices) {
                vecs[i].normalize()
                for (j in i + 1 until vecs.size) {
                    vecs[j].subtract(projectAndCreate(vecs[j], vecs[i]))
                }
            }
        }

        /**
         * Efficient Gram-Schmitt Ortho-normalization for the special case of 2 vectors.
         *
         * @param v1 The first [Vector3] object to be ortho-normalized.
         * @param v2 The second [Vector3]. [Vector3] object to be ortho-normalized.
         */
        fun orthoNormalize(/* @NonNull */ v1: Vector3, /* @NonNull */ v2: Vector3) {
            v1.normalize()
            v2.subtract(projectAndCreate(v2, v1))
            v2.normalize()
        }

        /**
         * Computes the Euclidean length of the arbitrary vector components passed in.
         *
         * @param x double The x component.
         * @param y double The y component.
         * @param z double The z component.
         *
         * @return double The Euclidean length.
         */
        fun length(x: Double, y: Double, z: Double): Double {
            return sqrt(length2(x, y, z))
        }

        /**
         * Computes the Euclidean length of the arbitrary vector components passed in.
         *
         * @param v [Vector3] The [Vector3] to calculate the length of.
         *
         * @return double The Euclidean length.
         */
        fun length(/* @NonNull */ v: Vector3): Double {
            return length(v.x, v.y, v.z)
        }

        /**
         * Computes the squared Euclidean length of the arbitrary vector components passed in.
         *
         * @param v [Vector3] The [Vector3] to calculate the length of.
         *
         * @return double The squared Euclidean length.
         */
        fun length2(/* @NonNull */ v: Vector3): Double {
            return length2(v.x, v.y, v.z)
        }

        /**
         * Computes the squared Euclidean length of the arbitrary vector components passed in.
         *
         * @param x double The x component.
         * @param y double The y component.
         * @param z double The z component.
         *
         * @return double The squared Euclidean length.
         */
        fun length2(x: Double, y: Double, z: Double): Double {
            return x * x + y * y + z * z
        }

        /**
         * Computes the Euclidean length between two [Vector3] objects.
         *
         * @param u [Vector3] The first vector.
         * @param v [Vector3] The second vector.
         *
         * @return double The Euclidean distance.
         */
        fun distanceTo(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Double {
            val a = u.x - v.x
            val b = u.y - v.y
            val c = u.z - v.z
            return sqrt(a * a + b * b + c * c)
        }

        /**
         * Computes the Euclidean length between two points.
         *
         * @return double The Euclidean distance.
         */
        fun distanceTo(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
            val a = x1 - x2
            val b = y1 - y2
            val c = z1 - z2
            return sqrt(a * a + b * b + c * c)
        }

        /**
         * Computes the squared Euclidean length between two [Vector3] objects.
         *
         * @param u [Vector3] The first vector.
         * @param v [Vector3] The second vector.
         *
         * @return double The squared Euclidean distance.
         */
        fun distanceTo2(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Double {
            val a = u.x - v.x
            val b = u.y - v.y
            val c = u.z - v.z
            return a * a + b * b + c * c
        }

        /**
         * Computes the squared Euclidean length between two points.
         *
         * @return double The squared Euclidean distance.
         */
        fun distanceTo2(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
            val a = x1 - x2
            val b = y1 - y2
            val c = z1 - z2
            return a * a + b * b + c * c
        }

        /**
         * Projects [Vector3] u onto [Vector3] v and creates a new
         * [Vector3] for the result.
         *
         * @param u [Vector3] to be projected.
         * @param v [Vector3] the [Vector3] to be projected on.
         *
         * @return [Vector3] The result of the projection.
         */
        /* @NonNull */
        fun projectAndCreate(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
            val d = u.dot(v)
            val d_div = d / v.length2()
            return v.clone().multiply(d_div)
        }

        /**
         * Computes the vector dot product between the two specified [Vector3] objects.
         *
         * @param u The first [Vector3].
         * @param v The second [Vector3].
         *
         * @return double The dot product.
         */
        fun dot(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Double {
            return u.x * v.x + u.y * v.y + u.z * v.z
        }

        /**
         * Computes the vector dot product between the components of the two supplied vectors.
         *
         * @param x1 double The x component of the first vector.
         * @param y1 double The y component of the first vector.
         * @param z1 double The z component of the first vector.
         * @param x2 double The x component of the second vector.
         * @param y2 double The y component of the second vector.
         * @param z2 double The z component of the second vector.
         *
         * @return double The dot product.
         */
        fun dot(x1: Double, y1: Double, z1: Double,
                x2: Double, y2: Double, z2: Double): Double {
            return x1 * x2 + y1 * y2 + z1 * z2
        }

        /**
         * Computes the cross product between two [Vector3] objects and and sets
         * a new [Vector3] to the result.
         *
         * @param u [Vector3] The first [Vector3] to cross.
         * @param v [Vector3] The second [Vector3] to cross.
         *
         * @return [Vector3] The computed cross product.
         */
        /* @NonNull */
        fun crossAndCreate(/* @NonNull */ u: Vector3, /* @NonNull */ v: Vector3): Vector3 {
            return Vector3(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x)
        }

        /**
         * Performs a linear interpolation between from and to by the specified amount.
         * The result will be stored in a new [Vector3] object.
         *
         * @param from   [Vector3] Starting point.
         * @param to     [Vector3] Ending point.
         * @param amount double [0-1] interpolation value.
         *
         * @return [Vector3] The interpolated value.
         */
        /* @NonNull */
        fun lerpAndCreate(/* @NonNull */ from: Vector3, /* @NonNull */ to: Vector3, amount: Double): Vector3 {
            val out = Vector3()
            out.x = from.x + (to.x - from.x) * amount
            out.y = from.y + (to.y - from.y) * amount
            out.z = from.z + (to.z - from.z) * amount
            return out
        }

        /**
         * Determines and returns the [Vector3] pointing along the
         * specified axis.
         * DO NOT MODIFY THE VALUES OF THE RETURNED VECTORS. DOING SO WILL HAVE
         * DRAMATICALLY UNDESIRED CONSEQUENCES.
         *
         * @param axis [Axis] the axis to find.
         *
         * @return [Vector3] the [Vector3] representing the requested axis.
         */
        /* @NonNull */
        fun getAxisVector(/* @NonNull */ axis: Axis): Vector3 {
            when (axis) {
                Axis.X -> return X
                Axis.Y -> return Y
                Axis.Z -> return Z
            }
        }
    }
}
