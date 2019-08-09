package org.rajawali3d.math

import org.junit.Test
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.math.vector.Vector3.Axis

import java.util.Arrays

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.rajawali3d.math.Quaternion.Companion.identity

/**
 * @author Jared Woolston (jwoolston@keywcorp.com)
 */
class QuaternionTest {

    @Test
    fun testConstructorNoArgs() {
        val q = Quaternion()
        assertNotNull(q)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testConstructorDoubles() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        assertNotNull(q)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(3.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(4.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testConstructorQuat() {
        val from = Quaternion(1.0, 2.0, 3.0, 4.0)
        val q = Quaternion(from)
        assertNotNull(q)
        assertTrue(from !== q)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(3.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(4.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testConstructorVector3AxisAngle() {
        val q = Quaternion(Vector3(1.0, 2.0, 3.0), 60.0)
        assertNotNull(q)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(0.13363062095621219234227674043988, q.x, 1e-14)
        assertEquals(0.26726124191242438468455348087975, q.y, 1e-14)
        assertEquals(0.40089186286863657702683022131963, q.z, 1e-14)
    }

    @Test
    fun testSetAllFromDoubles() {
        val q = Quaternion()
        val out = q.setAll(0.0, 1.0, 2.0, 3.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(3.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testSetAllFromQuaternion() {
        val from = Quaternion(1.0, 2.0, 3.0, 4.0)
        val q = Quaternion()
        val out = q.setAll(from)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(3.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(4.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testFromAngleAxisAxisAngle() {
        val q = Quaternion()
        // Test X
        var out = q.fromAngleAxis(Axis.X, 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(0.5, q.x, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        // Test Y
        q.identity()
        out = q.fromAngleAxis(Axis.Y, 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(0.5, q.y, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        // Test Z
        q.identity()
        out = q.fromAngleAxis(Axis.Z, 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(0.5, q.z, 1e-14)
    }

    @Test
    fun testFromAngleAxisVector3Angle() {
        val q = Quaternion()
        // Test X
        var out = q.fromAngleAxis(Vector3(1.0, 0.0, 0.0), 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(0.5, q.x, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        // Test Y
        q.identity()
        out = q.fromAngleAxis(Vector3(0.0, 1.0, 0.0), 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(0.5, q.y, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        // Test Z
        q.identity()
        out = q.fromAngleAxis(Vector3(0.0, 0.0, 1.0), 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(0.5, q.z, 1e-14)
        // Test 0
        q.identity()
        out = q.fromAngleAxis(Vector3(0.0, 0.0, 0.0), 45.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testFromAngleAxisDoubles() {
        val q = Quaternion()
        // Test X
        var out = q.fromAngleAxis(1.0, 0.0, 0.0, 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(0.5, q.x, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        // Test Y
        q.identity()
        out = q.fromAngleAxis(0.0, 1.0, 0.0, 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(0.5, q.y, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        // Test Z
        q.identity()
        out = q.fromAngleAxis(0.0, 0.0, 1.0, 60.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.86602540378443864676372317075294, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(0.5, q.z, 1e-14)
    }

    @Test
    fun testFromAxesVector3() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        // Try normal axes
        val out = q.fromAxes(Vector3.X, Vector3.Y, Vector3.Z)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))

        // Try moving the axes
        val out2 = q.fromAxes(Vector3.NEG_Z, Vector3.NEG_X, Vector3.NEG_Y)
        assertSame(q, out2)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.5), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(-0.5), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(-0.5), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(-0.5), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testFromAxesDoubles() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        // Try normal axes
        val out = q.fromAxes(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))

        // Try moving the axes
        val out2 = q.fromAxes(0.0, 0.0, -1.0, -1.0, 0.0, 0.0, 0.0, -1.0, 0.0)
        assertSame(q, out2)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.5), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(-0.5), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(-0.5), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(-0.5), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testFromMatrix() {
        val doubles = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 2.0, 3.0, -1.0, 1.0)
        val matrix = Matrix4(doubles)
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.fromMatrix(matrix)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.8956236623427759, q.w, 1e-14)
        assertEquals(-0.1674810596312623, q.x, 1e-14)
        assertEquals(-0.21493402652678664, q.y, 1e-14)
        assertEquals(-0.35171022522565076, q.z, 1e-14)
    }

    @Test
    fun testFromMatrixDoubles() {
        val doubles = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 2.0, 3.0, -1.0, 1.0)
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.fromMatrix(doubles)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.8956236623427759, q.w, 1e-14)
        assertEquals(-0.1674810596312623, q.x, 1e-14)
        assertEquals(-0.21493402652678664, q.y, 1e-14)
        assertEquals(-0.35171022522565076, q.z, 1e-14)
    }

    @Test
    fun testFromEuler() {
        val q = Quaternion()
        val out = q.fromEuler(30.0, 10.0, 40.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.911934541974004, q.w, 1e-14)
        assertEquals(0.16729342336556524, q.x, 1e-14)
        assertEquals(0.2134915560915891, q.y, 1e-14)
        assertEquals(0.3079117684189503, q.z, 1e-14)
    }

    @Test
    fun testFromRotationBetweenVector3() {
        val q = Quaternion()
        val out = q.fromRotationBetween(Vector3.X, Vector3.Y)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.7071067811865475, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(0.7071067811865475, q.z, 1e-14)
        val out1 = q.fromRotationBetween(Vector3.Y, Vector3.Z)
        assertNotNull(out1)
        assertSame(q, out1)
        assertEquals(0.7071067811865475, q.w, 1e-14)
        assertEquals(0.7071067811865475, q.x, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        val out2 = q.fromRotationBetween(Vector3.X, Vector3.Z)
        assertNotNull(out2)
        assertSame(q, out2)
        assertEquals(0.7071067811865475, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(-0.7071067811865475, q.y, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        val out3 = q.fromRotationBetween(Vector3.X, Vector3.X)
        assertNotNull(out3)
        assertSame(q, out3)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
        val out4 = q.fromRotationBetween(Vector3.X, Vector3.NEG_X)
        assertNotNull(out4)
        assertSame(q, out4)
        assertEquals(0.0, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(-1.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testFromRotationBetweenDoubles() {
        val q = Quaternion()
        val out = q.fromRotationBetween(1.0, 0.0, 0.0, 0.0, 1.0, 0.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.7071067811865475, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(0.7071067811865475, q.z, 1e-14)
    }

    @Test
    fun testCreateFromRotationBetween() {
        val q = Quaternion.createFromRotationBetween(Vector3.X, Vector3.Y)
        assertNotNull(q)
        assertEquals(0.7071067811865475, q.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(0.7071067811865475, q.z, 1e-14)
    }

    @Test
    fun testAdd() {
        val a = Quaternion()
        val b = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = a.add(b)
        assertNotNull(out)
        assertSame(a, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(a.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(a.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(3.0), java.lang.Double.doubleToRawLongBits(a.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(4.0), java.lang.Double.doubleToRawLongBits(a.z))
    }

    @Test
    fun testSubtract() {
        val a = Quaternion()
        val b = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = a.subtract(b)
        assertNotNull(out)
        assertSame(a, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(a.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(-2.0), java.lang.Double.doubleToRawLongBits(a.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(-3.0), java.lang.Double.doubleToRawLongBits(a.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(-4.0), java.lang.Double.doubleToRawLongBits(a.z))
    }

    @Test
    fun testMultiplyScalar() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.multiply(2.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(4.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(6.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(8.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testMultiplyQuat() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val m = Quaternion(5.0, 6.0, 7.0, 8.0)
        val out = q.multiply(m)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(-60.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(12.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(30.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(24.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testMultiplyVector3() {
        val q = Quaternion()
        val v = Vector3(Vector3.X)
        v.multiply(2.0)
        val out = q.multiply(v)
        assertNotNull(out)
        assertTrue(out !== v)
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out.z))
        q.fromAngleAxis(Axis.Z, 45.0)
        val out1 = q.multiply(v)
        assertNotNull(out1)
        assertTrue(out1 !== v)
        assertEquals(1.4142135623730951, out1.x, 1e-14)
        assertEquals(1.4142135623730951, out1.y, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out1.z))
        q.fromAngleAxis(1.0, 0.0, 1.0, 45.0)
        q.normalize()
        val out2 = q.multiply(v)
        assertNotNull(out2)
        assertTrue(out2 !== v)
        assertEquals(1.7071067811865477, out2.x, 1e-14)
        assertEquals(0.9999999999999998, out2.y, 1e-14)
        assertEquals(0.29289321881345237, out2.z, 1e-14)
    }

    @Test
    fun testMultiplyLeft() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val q1 = Quaternion(q)
        val m = Quaternion(5.0, 6.0, 7.0, 8.0)
        val m1 = Quaternion(m)
        val out = q.multiplyLeft(m)
        val expected = m1.multiply(q1)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(expected.w), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(expected.x), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(expected.y), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(expected.z), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testNormalize() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val norm = q.normalize()
        assertEquals(java.lang.Double.doubleToRawLongBits(30.0), java.lang.Double.doubleToRawLongBits(norm))
        assertEquals(0.1825741858350553711523232609336, q.w, 1e-14)
        assertEquals(0.3651483716701107423046465218672, q.x, 1e-14)
        assertEquals(0.5477225575051661134569697828008, q.y, 1e-14)
        assertEquals(0.7302967433402214846092930437344, q.z, 1e-14)
    }

    @Test
    fun testConjugate() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.conjugate()
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(-2.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(-3.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(-4.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testInverse() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.inverse()
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.03333333333333333, q.w, 1e-14)
        assertEquals(-0.06666666666666667, q.x, 1e-14)
        assertEquals(-0.1, q.y, 1e-14)
        assertEquals(-0.13333333333333333, q.z, 1e-14)
    }

    @Test
    fun testInvertAndCreate() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.invertAndCreate()
        assertNotNull(out)
        assertTrue(out !== q)
        assertEquals(0.03333333333333333, out.w, 1e-14)
        assertEquals(-0.06666666666666667, out.x, 1e-14)
        assertEquals(-0.1, out.y, 1e-14)
        assertEquals(-0.13333333333333333, out.z, 1e-14)
    }

    @Test
    fun testComputeW() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.computeW()
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(-0.84261497731763586306341399062027, q.w, 1e-14)
        assertEquals(0.2, q.x, 1e-14)
        assertEquals(0.3, q.y, 1e-14)
        assertEquals(0.4, q.z, 1e-14)
    }

    @Test
    fun testGetXAxis() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.X, 10.0)
        q.multiply(Quaternion().fromAngleAxis(Axis.Y, 30.0))
        q.multiply(Quaternion().fromAngleAxis(Axis.Z, 40.0))
        val v = q.xAxis
        assertNotNull(v)
        assertEquals(0.6634139481689384, v.x, 1e-14)
        assertEquals(0.6995333323392336, v.y, 1e-14)
        assertEquals(-0.2655843563187949, v.z, 1e-14)
    }

    @Test
    fun testGetYAxis() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.X, 10.0)
        q.multiply(Quaternion().fromAngleAxis(Axis.Y, 30.0))
        q.multiply(Quaternion().fromAngleAxis(Axis.Z, 40.0))
        val v = q.yAxis
        assertNotNull(v)
        assertEquals(-0.5566703992264195, v.x, 1e-14)
        assertEquals(0.6985970582110141, v.y, 1e-14)
        assertEquals(0.44953333233923354, v.z, 1e-14)
    }

    @Test
    fun testGetZAxis() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.X, 10.0)
        q.multiply(Quaternion().fromAngleAxis(Axis.Y, 30.0))
        q.multiply(Quaternion().fromAngleAxis(Axis.Z, 40.0))
        val v = q.zAxis
        assertNotNull(v)
        assertEquals(0.5, v.x, 1e-14)
        assertEquals(-0.1503837331804353, v.y, 1e-14)
        assertEquals(0.8528685319524432, v.z, 1e-14)
    }

    @Test
    fun testGetAxis() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.X, 10.0)
        q.multiply(Quaternion().fromAngleAxis(Axis.Y, 30.0))
        q.multiply(Quaternion().fromAngleAxis(Axis.Z, 40.0))
        var v = q.getAxis(Axis.X)
        assertNotNull(v)
        assertEquals(0.6634139481689384, v.x, 1e-14)
        assertEquals(0.6995333323392336, v.y, 1e-14)
        assertEquals(-0.2655843563187949, v.z, 1e-14)
        v = q.getAxis(Axis.Y)
        assertNotNull(v)
        assertEquals(-0.5566703992264195, v.x, 1e-14)
        assertEquals(0.6985970582110141, v.y, 1e-14)
        assertEquals(0.44953333233923354, v.z, 1e-14)
        v = q.getAxis(Axis.Z)
        assertNotNull(v)
        assertEquals(0.5, v.x, 1e-14)
        assertEquals(-0.1503837331804353, v.y, 1e-14)
        assertEquals(0.8528685319524432, v.z, 1e-14)
    }

    @Test
    fun testLength() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        assertEquals(Math.sqrt(30.0), q.length(), 1e-14)
    }

    @Test
    fun testLength2() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        assertEquals(30.0, q.length2(), 1e-14)
    }

    @Test
    fun testDot() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val b = Quaternion(5.0, 6.0, 7.0, 8.0)
        assertEquals(java.lang.Double.doubleToRawLongBits(70.0), java.lang.Double.doubleToRawLongBits(q.dot(b)))
    }

    @Test
    fun testIdentity() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.identity()
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testGetIdentity() {
        val q : Quaternion = identity
        assertNotNull(q)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testExp() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.exp()
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.8584704679084777, q.w, 1e-14)
        assertEquals(0.1904725360704355, q.x, 1e-14)
        assertEquals(0.2857088041056532, q.y, 1e-14)
        assertEquals(0.380945072140871, q.z, 1e-14)
    }

    @Test
    fun testExpAndCreate() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.expAndCreate()
        assertNotNull(out)
        assertTrue(out !== q)
        assertEquals(0.8584704679084777, out.w, 1e-14)
        assertEquals(0.1904725360704355, out.x, 1e-14)
        assertEquals(0.2857088041056532, out.y, 1e-14)
        assertEquals(0.380945072140871, out.z, 1e-14)
    }

    @Test
    fun testLog() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.log()
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.1273211091867903, q.w, 1e-14)
        assertEquals(0.18346103730565178, q.x, 1e-14)
        assertEquals(0.2751915559584776, q.y, 1e-14)
        assertEquals(0.36692207461130355, q.z, 1e-14)
        q.setAll(0.0, 0.0, 0.0, 0.0)
        val out2 = q.log()
        assertNotNull(out2)
        assertSame(q, out2)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(q.z))
    }

    @Test
    fun testLogAndCreate() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.logAndCreate()
        assertNotNull(out)
        assertTrue(out !== q)
        assertEquals(0.1273211091867903, out.w, 1e-14)
        assertEquals(0.18346103730565178, out.x, 1e-14)
        assertEquals(0.2751915559584776, out.y, 1e-14)
        assertEquals(0.36692207461130355, out.z, 1e-14)
        q.setAll(0.0, 0.0, 0.0, 0.0)
        val out2 = q.logAndCreate()
        assertNotNull(out2)
        assertTrue(out2 !== q)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out2.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out2.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out2.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out2.z))
    }

    @Test
    fun testPowDouble() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.pow(2.0)
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.71, q.w, 1e-14)
        assertEquals(0.4, q.x, 1e-14)
        assertEquals(0.6, q.y, 1e-14)
        assertEquals(0.8, q.z, 1e-14)
    }

    @Test
    fun testPowQuaternion() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.pow(Quaternion(1.0, 2.0, 3.0, 4.0))
        assertNotNull(out)
        assertSame(q, out)
        assertEquals(0.3812677186385471, q.w, 1e-14)
        assertEquals(0.3433375591865236, q.x, 1e-14)
        assertEquals(0.5150063387797856, q.y, 1e-14)
        assertEquals(0.6866751183730476, q.z, 1e-14)
    }

    @Test
    fun testPowAndCreateDouble() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.powAndCreate(2.0)
        assertNotNull(out)
        assertTrue(out !== q)
        assertEquals(0.71, out.w, 1e-14)
        assertEquals(0.4, out.x, 1e-14)
        assertEquals(0.6, out.y, 1e-14)
        assertEquals(0.8, out.z, 1e-14)
    }

    @Test
    fun testPowAndCreateQuaternion() {
        val q = Quaternion(1.0, 0.2, 0.3, 0.4)
        val out = q.powAndCreate(Quaternion(1.0, 2.0, 3.0, 4.0))
        assertNotNull(out)
        assertTrue(out !== q)
        assertEquals(0.3812677186385471, out.w, 1e-14)
        assertEquals(0.3433375591865236, out.x, 1e-14)
        assertEquals(0.5150063387797856, out.y, 1e-14)
        assertEquals(0.6866751183730476, out.z, 1e-14)
    }

    @Test
    fun testSlerpQuaternion() {
        val start = Quaternion(Vector3.X, 0.0)
        val end = Quaternion(Vector3.X, 90.0)
        val middle = Quaternion(0.9238795325112868, 0.3826834323650898, 0.0, 0.0)
        var out = start.slerp(end, 0.0)
        assertNotNull(out)
        assertSame(out, start)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = start.slerp(end, 0.5)
        assertNotNull(out)
        assertSame(out, start)
        assertEquals(middle.w, out.w, 1e-14)
        assertEquals(middle.x, out.x, 1e-14)
        assertEquals(middle.y, out.y, 1e-14)
        assertEquals(middle.z, out.z, 1e-14)
        start.fromAngleAxis(Vector3.X, 0.0)
        out = start.slerp(end, 1.0)
        assertNotNull(out)
        assertSame(out, start)
        assertEquals(end.w, out.w, 1e-14)
        assertEquals(end.x, out.x, 1e-14)
        assertEquals(end.y, out.y, 1e-14)
        assertEquals(end.z, out.z, 1e-14)
    }

    @Test
    fun testSlerpTwoQuaternions() {
        val q = Quaternion()
        val start = Quaternion(Vector3.X, 0.0)
        val end = Quaternion(Vector3.X, 90.0)
        val middle = Quaternion(0.9238795325112868, 0.3826834323650898, 0.0, 0.0)
        var out = q.slerp(start, start, 0.0)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = q.slerp(start, end, 0.0)
        assertNotNull(out)
        assertSame(out, q)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        q.identity()
        out = q.slerp(start, end, 0.5)
        assertNotNull(out)
        assertSame(out, q)
        assertEquals(middle.w, out.w, 1e-14)
        assertEquals(middle.x, out.x, 1e-14)
        assertEquals(middle.y, out.y, 1e-14)
        assertEquals(middle.z, out.z, 1e-14)
        q.identity()
        out = q.slerp(start, end, 1.0)
        assertNotNull(out)
        assertSame(out, q)
        assertEquals(end.w, out.w, 1e-14)
        assertEquals(end.x, out.x, 1e-14)
        assertEquals(end.y, out.y, 1e-14)
        assertEquals(end.z, out.z, 1e-14)
    }

    @Test
    fun testSlerpTwoQuaternionsShortestPath() {
        val q = Quaternion()
        val start = Quaternion(Vector3.X, 0.0)
        val end = Quaternion(Vector3.X, 90.0)
        val middle = Quaternion(0.9238795325112868, 0.3826834323650898, 0.0, 0.0)
        var out = q.slerp(start, start, 0.0, true)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = q.slerp(start, end, 0.0, true)
        assertNotNull(out)
        assertSame(out, q)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        q.identity()
        out = q.slerp(start, end, 0.5, true)
        assertNotNull(out)
        assertSame(out, q)
        assertEquals(middle.w, out.w, 1e-14)
        assertEquals(middle.x, out.x, 1e-14)
        assertEquals(middle.y, out.y, 1e-14)
        assertEquals(middle.z, out.z, 1e-14)
        q.identity()
        out = q.slerp(start, end, 1.0, true)
        assertNotNull(out)
        assertSame(out, q)
        assertEquals(end.w, out.w, 1e-14)
        assertEquals(end.x, out.x, 1e-14)
        assertEquals(end.y, out.y, 1e-14)
        assertEquals(end.z, out.z, 1e-14)
    }

    @Test
    fun testLerp() {
        val start = Quaternion(Vector3.X, 0.0)
        val end = Quaternion(Vector3.X, 90.0)
        val middle = Quaternion(0.8535533905932737, 0.35355339059327373, 0.0, 0.0)
        var out = Quaternion.lerp(start, start, 0.0, true)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = Quaternion.lerp(start, end, 0.0, true)
        assertNotNull(out)
        assertEquals(java.lang.Double.doubleToRawLongBits(start.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(start.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = Quaternion.lerp(start, end, 0.5, true)
        assertNotNull(out)
        assertEquals(middle.w, out.w, 1e-14)
        assertEquals(middle.x, out.x, 1e-14)
        assertEquals(middle.y, out.y, 1e-14)
        assertEquals(middle.z, out.z, 1e-14)
        out = Quaternion.lerp(start, end, 1.0, true)
        assertNotNull(out)
        assertEquals(end.w, out.w, 1e-14)
        assertEquals(end.x, out.x, 1e-14)
        assertEquals(end.y, out.y, 1e-14)
        assertEquals(end.z, out.z, 1e-14)
    }

    @Test
    fun testNlerp() {
        val start = Quaternion(Vector3.X, 0.0)
        start.multiply(2.0)
        val nStart = start.clone()
        nStart.normalize()
        val end = Quaternion(Vector3.X, 90.0)
        end.multiply(2.0)
        val nEnd = end.clone()
        nEnd.normalize()
        val middle = Quaternion(0.8535533905932737, 0.35355339059327373, 0.0, 0.0)
        middle.multiply(2.0)
        val nMiddle = middle.clone()
        nMiddle.normalize()
        var out = Quaternion.nlerp(start, start, 0.0, true)
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = Quaternion.nlerp(start, end, 0.0, true)
        assertNotNull(out)
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.w), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.x), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.y), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(nStart.z), java.lang.Double.doubleToRawLongBits(out.z))
        out = Quaternion.nlerp(start, end, 0.5, true)
        assertNotNull(out)
        assertEquals("" + out, nMiddle.w, out.w, 1e-14)
        assertEquals(nMiddle.x, out.x, 1e-14)
        assertEquals(nMiddle.y, out.y, 1e-14)
        assertEquals(nMiddle.z, out.z, 1e-14)
        out = Quaternion.nlerp(start, end, 1.0, true)
        assertNotNull(out)
        assertEquals(nEnd.w, out.w, 1e-14)
        assertEquals(nEnd.x, out.x, 1e-14)
        assertEquals(nEnd.y, out.y, 1e-14)
        assertEquals(nEnd.z, out.z, 1e-14)
    }

    @Test
    fun testGetGimbalPole() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.X, 90.0)
        assertEquals(q.gimbalPole.toLong(), 0)
        q.fromAngleAxis(Axis.X, 90.0).multiply(Quaternion().fromAngleAxis(Axis.Y, 90.0))
        assertEquals(q.gimbalPole.toLong(), 1)
        q.fromAngleAxis(Axis.X, 90.0).multiply(Quaternion().fromAngleAxis(Axis.Y, -90.0))
        assertEquals(q.gimbalPole.toLong(), -1)
    }

    @Test
    fun testGetRotationX() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.X, 45.0)
        assertEquals(MathUtil.degreesToRadians(45.0), q.rotationX, 1e-14)
    }

    @Test
    fun testGetRotationY() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.Y, 45.0)
        assertEquals(MathUtil.degreesToRadians(45.0), q.rotationY, 1e-14)
    }

    @Test
    fun testGetRotationZ() {
        val q = Quaternion()
        q.fromAngleAxis(Axis.Z, 45.0)
        assertEquals(MathUtil.degreesToRadians(45.0), q.rotationZ, 1e-14)
    }

    @Test
    fun testToRotationMatrix() {
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val q = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val out = q.toRotationMatrix()
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testToRotationMatrixMatrix4() {
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val q = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val m = Matrix4()
        val out = q.toRotationMatrix(m)
        assertNotNull(out)
        assertSame(m, out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testToRotationMatrixDoubles() {
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val q = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val result = DoubleArray(16)
        q.toRotationMatrix(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    // FIXME Tests fail on some platforms
    /*@Test
    public void testLookAt() {
        final Quaternion q = new Quaternion(1d, 2d, 3d, 4d);
        final Vector3 lookAt = Vector3.subtractAndCreate(new Vector3(0, 10d, 10d), Vector3.ZERO);
        final Vector3 up = Vector3.Y;
        Quaternion out = q.lookAt(lookAt, up);
        assertNotNull(out);
        assertSame(q, out);
        assertEquals(0.9238795325112867, out.w, 1e-14);
        assertEquals(0.3826834323650898, out.x, 1e-14);
        assertEquals(0d, out.y, 1e-14);
        assertEquals(0d, out.z, 1e-14);

        lookAt.subtractAndSet(new Vector3(10d, 0, 10d), Vector3.ZERO);
        out = q.lookAt(lookAt, up);
        assertNotNull(out);
        assertSame(q, out);
        assertEquals(0.9238795325112867, out.w, 1e-14);
        assertEquals(0d, out.x, 1e-14);
        assertEquals(-0.3826834323650898, out.y, 1e-14);
        assertEquals(0d, out.z, 1e-14);

        lookAt.subtractAndSet(new Vector3(0d, 10d, 0d), Vector3.ZERO);
        out = q.lookAt(lookAt, Vector3.Y);
        assertNotNull(out);
        assertSame(q, out);
        assertEquals(0.7071067811865475, out.w, 1e-14);
        assertEquals(0d, out.x, 1e-14);
        assertEquals(0d, out.y, 1e-14);
        assertEquals(0.7071067811865475, out.z, 1e-14);

        lookAt.subtractAndSet(new Vector3(0d, 10d, 0d), Vector3.ZERO);
        out = q.lookAt(lookAt, Vector3.NEG_Y);
        assertNotNull(out);
        assertSame(q, out);
        assertEquals(0.7071067811865475, out.w, 1e-14);
        assertEquals(0d, out.x, 1e-14);
        assertEquals(0d, out.y, 1e-14);
        assertEquals(-0.7071067811865475, out.z, 1e-14);
    }

    @Test
    public void testLookAtAndCreate() {
        final Vector3 lookAt = Vector3.subtractAndCreate(new Vector3(0, 10d, 10d), Vector3.ZERO);
        final Vector3 up = Vector3.Y;
        Quaternion out = Quaternion.lookAtAndCreate(lookAt, up);
        assertNotNull(out);
        assertEquals(0.9238795325112867, out.w, 1e-14);
        assertEquals(0.3826834323650898, out.x, 1e-14);
        assertEquals(0d, out.y, 1e-14);
        assertEquals(0d, out.z, 1e-14);

        lookAt.subtractAndSet(new Vector3(10d, 0, 10d), Vector3.ZERO);
        out = Quaternion.lookAtAndCreate(lookAt, up);
        assertNotNull(out);
        assertEquals(0.9238795325112867, out.w, 1e-14);
        assertEquals(0d, out.x, 1e-14);
        assertEquals(-0.3826834323650898, out.y, 1e-14);
        assertEquals(0d, out.z, 1e-14);

        lookAt.subtractAndSet(new Vector3(0d, 10d, 0d), Vector3.ZERO);
        out = Quaternion.lookAtAndCreate(lookAt, Vector3.Y);
        assertNotNull(out);
        assertEquals(0.7071067811865475, out.w, 1e-14);
        assertEquals(0d, out.x, 1e-14);
        assertEquals(0d, out.y, 1e-14);
        assertEquals(0.7071067811865475, out.z, 1e-14);

        lookAt.subtractAndSet(new Vector3(0d, 10d, 0d), Vector3.ZERO);
        out = Quaternion.lookAtAndCreate(lookAt, Vector3.NEG_Y);
        assertNotNull(out);
        assertEquals(0.7071067811865475, out.w, 1e-14);
        assertEquals(0d, out.x, 1e-14);
        assertEquals(0d, out.y, 1e-14);
        assertEquals(-0.7071067811865475, out.z, 1e-14);
    }*/

    @Test
    fun testAngleBetween() {
        val q1 = Quaternion(Vector3.X, 0.0)
        val q2 = Quaternion(Vector3.X, 45.0)
        val angle = q1.angleBetween(q2)
        assertEquals(MathUtil.degreesToRadians(45.0), angle, 1e-14)
    }

    @Test
    fun testClone() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val out = q.clone()
        assertNotNull(out)
        assertTrue(out !== q)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(out.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(2.0), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(3.0), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(4.0), java.lang.Double.doubleToRawLongBits(out.z))
    }

    @Test
    fun testEquals() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val other = Quaternion(q)
        assertEquals(q, other)
    }

    @Test
    fun testEqualsWithTolerance() {
        val q = Quaternion(1.0000001, 2.0, 3.0, 4.0)
        val other = Quaternion(1.0, 2.0, 3.0, 4.0)
        q.normalize()
        other.normalize()
        assertTrue(q.equals(other, 1e-6))
    }

    @Test
    fun testToString() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val s = q.toString()
        assertNotNull(s)
        assertEquals("Quaternion <w, x, y, z>: <1.0, 2.0, 3.0, 4.0>", s)
    }

    @Test
    fun testCommutativity() {
        val p = Quaternion(4.0, 3.0, 2.0, 1.0)
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)

        // p + q = q + p
        assertEquals(
                p.clone().add(q).w,
                q.clone().add(p).w,
                1e-14
        )
        assertEquals(
                p.clone().add(q).x,
                q.clone().add(p).x,
                1e-14
        )
        assertEquals(
                p.clone().add(q).y,
                q.clone().add(p).y,
                1e-14
        )
        assertEquals(
                p.clone().add(q).z,
                q.clone().add(p).z,
                1e-14
        )
    }

    @Test
    fun testAdditiveIdentity() {
        val p = Quaternion(1.0, 2.0, 3.0, 4.0)
        val zero = Quaternion(0.0, 0.0, 0.0, 0.0)

        // p = p + 0
        assertEquals(
                p.w,
                p.clone().add(zero).w,
                1e-14
        )
        assertEquals(
                p.x,
                p.clone().add(zero).x,
                1e-14
        )
        assertEquals(
                p.y,
                p.clone().add(zero).y,
                1e-14
        )
        assertEquals(
                p.z,
                p.clone().add(zero).z,
                1e-14
        )

        // p = 0 + p
        assertEquals(
                p.w,
                zero.clone().add(p).w,
                1e-14
        )
        assertEquals(
                p.x,
                zero.clone().add(p).x,
                1e-14
        )
        assertEquals(
                p.y,
                zero.clone().add(p).y,
                1e-14
        )
        assertEquals(
                p.z,
                zero.clone().add(p).z,
                1e-14
        )
    }

    @Test
    fun testAdditiveAssociativity() {
        val p = Quaternion(4.0, 3.0, 2.0, 1.0)
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val r = Quaternion(3.0, 4.0, 1.0, 2.0)

        // (p + q) + r = p + (q + r)
        assertEquals(
                p.clone().add(q).add(r).w,
                p.clone().add(q.clone().add(r)).w,
                1e-14
        )
        assertEquals(
                p.clone().add(q).add(r).x,
                p.clone().add(q.clone().add(r)).x,
                1e-14
        )
        assertEquals(
                p.clone().add(q).add(r).y,
                p.clone().add(q.clone().add(r)).y,
                1e-14
        )
        assertEquals(
                p.clone().add(q).add(r).z,
                p.clone().add(q.clone().add(r)).z,
                1e-14
        )
    }

    @Test
    fun testMultiplicativeIdentity() {
        val p = Quaternion(1.0, 2.0, 3.0, 4.0)
        val one = Quaternion().identity()

        // p = p * 1
        assertEquals(
                p.w,
                p.clone().multiply(one).w,
                1e-14
        )
        assertEquals(
                p.x,
                p.clone().multiply(one).x,
                1e-14
        )
        assertEquals(
                p.y,
                p.clone().multiply(one).y,
                1e-14
        )
        assertEquals(
                p.z,
                p.clone().multiply(one).z,
                1e-14
        )

        // p = 1 * p
        assertEquals(
                p.w,
                one.clone().multiply(p).w,
                1e-14
        )
        assertEquals(
                p.x,
                one.clone().multiply(p).x,
                1e-14
        )
        assertEquals(
                p.y,
                one.clone().multiply(p).y,
                1e-14
        )
        assertEquals(
                p.z,
                one.clone().multiply(p).z,
                1e-14
        )
    }

    @Test
    fun testMultiplicativeAssociativity() {
        val p = Quaternion(4.0, 3.0, 2.0, 1.0)
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val r = Quaternion(3.0, 4.0, 1.0, 2.0)

        // (p * q) * r = p * (q * r)
        assertEquals(
                p.clone().multiply(q).multiply(r).w,
                p.clone().multiply(q.clone().multiply(r)).w,
                1e-14
        )
        assertEquals(
                p.clone().multiply(q).multiply(r).x,
                p.clone().multiply(q.clone().multiply(r)).x,
                1e-14
        )
        assertEquals(
                p.clone().multiply(q).multiply(r).y,
                p.clone().multiply(q.clone().multiply(r)).y,
                1e-14
        )
        assertEquals(
                p.clone().multiply(q).multiply(r).z,
                p.clone().multiply(q.clone().multiply(r)).z,
                1e-14
        )
    }

    @Test
    fun testDistributivity() {
        val p = Quaternion(4.0, 3.0, 2.0, 1.0)
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val r = Quaternion(3.0, 4.0, 1.0, 2.0)

        // p * (q + r) = p * q + p * r)
        assertEquals(
                p.clone().multiply(q.clone().add(r)).w,
                p.clone().multiply(q).add(p.clone().multiply(r)).w,
                1e-14
        )
        assertEquals(
                p.clone().multiply(q.clone().add(r)).x,
                p.clone().multiply(q).add(p.clone().multiply(r)).x,
                1e-14
        )
        assertEquals(
                p.clone().multiply(q.clone().add(r)).y,
                p.clone().multiply(q).add(p.clone().multiply(r)).y,
                1e-14
        )
        assertEquals(
                p.clone().multiply(q.clone().add(r)).z,
                p.clone().multiply(q).add(p.clone().multiply(r)).z,
                1e-14
        )
    }

    @Test
    fun testInverseDivision() {
        val q = Quaternion(4.0, 3.0, 2.0, 1.0)
        val r = Quaternion(1.0, 2.0, 3.0, 4.0)
        val p = q.clone().multiply(r.clone())

        // from p = q * r follows that q = p * r.inverse() and r = q.inverse() * p
        assertEquals(q.w, p.clone().multiply(r.clone().inverse()).w, 1e-14)
        assertEquals(q.x, p.clone().multiply(r.clone().inverse()).x, 1e-14)
        assertEquals(q.y, p.clone().multiply(r.clone().inverse()).y, 1e-14)
        assertEquals(q.z, p.clone().multiply(r.clone().inverse()).z, 1e-14)

        assertEquals(r.w, q.clone().inverse().multiply(p).w, 1e-14)
        assertEquals(r.x, q.clone().inverse().multiply(p).x, 1e-14)
        assertEquals(r.y, q.clone().inverse().multiply(p).y, 1e-14)
        assertEquals(r.z, q.clone().inverse().multiply(p).z, 1e-14)
    }

    @Test
    fun testPowDivision() {
        val q = Quaternion(4.0, 3.0, 2.0, 1.0)
        val r = Quaternion(1.0, 2.0, 3.0, 4.0)
        q.normalize()
        r.normalize()
        val p = q.clone().multiply(r.clone())

        // for unit Quaternions, from p = q * r follows that q = p * r.pow(-1) and r = q.pow(-1) * p
        assertEquals(q.w, p.clone().multiply(r.clone().pow(-1.0)).w, 1e-14)
        assertEquals(q.x, p.clone().multiply(r.clone().pow(-1.0)).x, 1e-14)
        assertEquals(q.y, p.clone().multiply(r.clone().pow(-1.0)).y, 1e-14)
        assertEquals(q.z, p.clone().multiply(r.clone().pow(-1.0)).z, 1e-14)

        assertEquals(r.w, q.clone().pow(-1.0).multiply(p).w, 1e-14)
        assertEquals(r.x, q.clone().pow(-1.0).multiply(p).x, 1e-14)
        assertEquals(r.y, q.clone().pow(-1.0).multiply(p).y, 1e-14)
        assertEquals(r.z, q.clone().pow(-1.0).multiply(p).z, 1e-14)
    }

    @Test
    fun testSquare() {
        val p = Quaternion(1.0, 2.0, 3.0, 4.0)
        p.normalize()

        // for unit quaternions, p * p = p.pow(2)
        val p2 = p.clone().multiply(p)
        val pow2 = p.clone().pow(2.0)

        assertEquals(p2.w, pow2.w, 1e-14)
        assertEquals(p2.x, pow2.x, 1e-14)
        assertEquals(p2.y, pow2.y, 1e-14)
        assertEquals(p2.z, pow2.z, 1e-14)
    }
}
