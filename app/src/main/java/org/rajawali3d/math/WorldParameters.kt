@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package org.rajawali3d.math

import org.rajawali3d.math.vector.Vector3

/**
 * Collection of world global parameters. These parameters are constant across all scenes. This class is intended to be
 * read only after setup, and so does not include any thread safety mechanisms in the interest of speed. Extreme care
 * must be taken if you desire to modify anything in this class while other threads are actively using it.
 *
 * @author Jared Woolston (jwoolston@idealcorp.com)
 */
object WorldParameters {

    private val TEMP_VECTOR = Vector3()

    /**
     * Global Right Axis. Defaults to OpenGL +X axis. It is not safe to modify this [Vector3] directly.
     * Instead, use [.setWorldAxes].
     */
    val RIGHT_AXIS = Vector3.X.clone()

    /**
     * Global Negative Right Axis. Defaults to OpenGL -X axis. It is not safe to modify this [Vector3] directly.
     * Instead, use [.setWorldAxes].
     */
    val NEG_RIGHT_AXIS = Vector3.NEG_X.clone()

    /**
     * Global Up Axis. Defaults to OpenGL +Y axis. It is not safe to modify this [Vector3] directly.
     * Instead, use [.setWorldAxes].
     */
    val UP_AXIS = Vector3.Y.clone()

    /**
     * Global Negative Up Axis. Defaults to OpenGL -Y axis. It is not safe to modify this [Vector3] directly.
     * Instead, use [.setWorldAxes].
     */
    val NEG_UP_AXIS = Vector3.NEG_Y.clone()

    /**
     * Global Forward Axis. Defaults to OpenGL +Z axis. It is not safe to modify this [Vector3] directly.
     * Instead, use [.setWorldAxes].
     */
    val FORWARD_AXIS = Vector3.Z.clone()

    /**
     * Global Negative Forward Axis. Defaults to OpenGL -Z axis. It is not safe to modify this [Vector3] directly.
     * Instead, use [.setWorldAxes].
     */
    val NEG_FORWARD_AXIS = Vector3.NEG_Z.clone()

    /**
     * Sets the world axis values after checking that they are all orthogonal to each other. The check performed
     * is to verify that the cross product between `right` and `up` is equivilant to `forward`
     * withing 1ppm error on each component.
     *
     * @param right [Vector3] The desired right vector. Must be normalized.
     * @param up [Vector3] The desired up vector. Must be normalized.
     * @param forward [Vector3] The desired forward vector. Must be normalized.
     */
    fun setWorldAxes(right: Vector3, up: Vector3, forward: Vector3) {
        TEMP_VECTOR.crossAndSet(right, up)
        if (!TEMP_VECTOR.equals(forward, 1e-6)) {
            throw IllegalArgumentException("World axes must be orthogonal.")
        }
        RIGHT_AXIS.setAll(right)
        NEG_RIGHT_AXIS.setAll(RIGHT_AXIS).inverse()
        UP_AXIS.setAll(up)
        NEG_UP_AXIS.setAll(UP_AXIS).inverse()
        FORWARD_AXIS.setAll(forward)
        NEG_FORWARD_AXIS.setAll(FORWARD_AXIS).inverse()
    }
}
