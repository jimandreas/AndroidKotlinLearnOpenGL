package org.rajawali3d.math.vector

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.Quaternion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.rajawali3d.math.Quaternion.Companion.identity

/**
 * @author Jared Woolston (jwoolston@keywcorp.com)
 */
class Vector3Test {

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun testZero() {
        val zero = Vector3.ZERO
        assertNotNull(zero)
        assertEquals(0.0, zero.x, 0.0)
        assertEquals(0.0, zero.y, 0.0)
        assertEquals(0.0, zero.z, 0.0)
    }

    @Test
    fun testOne() {
        val one = Vector3.ONE
        assertNotNull(one)
        assertEquals(1.0, one.x, 0.0)
        assertEquals(1.0, one.y, 0.0)
        assertEquals(1.0, one.z, 0.0)
    }

    @Test
    fun testConstructorNoArgs() {
        val v = Vector3()
        assertNotNull(v)
        assertEquals(0.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
    }

    @Test
    fun testConstructorFromDouble() {
        val v = Vector3(1.0)
        assertNotNull(v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(1.0, v.y, 0.0)
        assertEquals(1.0, v.z, 0.0)
    }

    @Test
    fun testConstructorFromVector3() {
        val v1 = Vector3(2.0)
        val v = Vector3(v1)
        assertNotNull(v)
        assertEquals(2.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(2.0, v.z, 0.0)
    }

    @Test
    fun testConstructorFromStringArray() {
        val values = arrayOf("1", "2", "3")
        val v = Vector3(values)
        assertNotNull(v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testConstructorFromDoubleArray() {
        val values = doubleArrayOf(1.0, 2.0, 3.0)
        val v = Vector3(values)
        assertNotNull(v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorFromShortDoubleArray() {
        val values = doubleArrayOf(1.0, 2.0)
        Vector3(values)
    }

    @Test
    fun testConstructorDoublesXyz() {
        val v = Vector3(1.0, 2.0, 3.0)
        assertNotNull(v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testSetAllFromDoublesXyz() {
        val v = Vector3()
        assertNotNull(v)
        val out = v.setAll(1.0, 2.0, 3.0)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testSetAllFromVector3() {
        val v = Vector3()
        assertNotNull(v)
        val out = v.setAll(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testSetAllFromAxis() {
        val v = Vector3()
        assertNotNull(v)
        val outX = v.setAll(Vector3.Axis.X)
        assertNotNull(outX)
        assertTrue(outX === v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
        val outY = v.setAll(Vector3.Axis.Y)
        assertNotNull(outY)
        assertTrue(outY === v)
        assertEquals(0.0, v.x, 0.0)
        assertEquals(1.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
        val outZ = v.setAll(Vector3.Axis.Z)
        assertNotNull(outZ)
        assertTrue(outZ === v)
        assertEquals(0.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
        assertEquals(1.0, v.z, 0.0)
    }

    @Test
    fun testAddVector3() {
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(0.1, 0.2, 0.3)
        val out = u.add(v)
        assertNotNull(out)
        assertTrue(out === u)
        assertEquals(1.1, u.x, 0.0)
        assertEquals(2.2, u.y, 0.0)
        assertEquals(3.3, u.z, 0.0)
    }

    @Test
    fun testAddDoublesXyz() {
        val v = Vector3(1.0, 2.0, 3.0)
        val out = v.add(0.1, 0.2, 0.3)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(1.1, v.x, 0.0)
        assertEquals(2.2, v.y, 0.0)
        assertEquals(3.3, v.z, 0.0)
    }

    @Test
    fun testAddDouble() {
        val v = Vector3(1.0, 2.0, 3.0)
        val out = v.add(0.1)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(1.1, v.x, 0.0)
        assertEquals(2.1, v.y, 0.0)
        assertEquals(3.1, v.z, 0.0)
    }

    @Test
    fun testAddAndSet() {
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(0.1, 0.2, 0.3)
        val t = Vector3()
        val out = t.addAndSet(u, v)
        assertNotNull(out)
        assertTrue(out === t)
        assertEquals(1.1, t.x, 0.0)
        assertEquals(2.2, t.y, 0.0)
        assertEquals(3.3, t.z, 0.0)
    }

    @Test
    fun testAddAndCreate() {
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(0.1, 0.2, 0.3)
        val t = Vector3.addAndCreate(u, v)
        assertNotNull(t)
        assertEquals(1.1, t.x, 0.0)
        assertEquals(2.2, t.y, 0.0)
        assertEquals(3.3, t.z, 0.0)
    }

    @Test
    fun testSubtractVector3() {
        val u = Vector3(1.1, 2.2, 3.3)
        val v = Vector3(0.1, 0.2, 0.3)
        val out = u.subtract(v)
        assertNotNull(out)
        assertTrue(out === u)
        assertEquals(1.0, u.x, 0.0)
        assertEquals(2.0, u.y, 0.0)
        assertEquals(3.0, u.z, 0.0)
    }

    @Test
    fun testSubtractDoublesXyz() {
        val v = Vector3(1.1, 2.2, 3.3)
        val out = v.subtract(0.1, 0.2, 0.3)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testSubtractDouble() {
        val v = Vector3(1.1, 2.1, 3.1)
        val out = v.subtract(0.1)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testSubtractAndSet() {
        val u = Vector3(1.1, 2.2, 3.3)
        val v = Vector3(0.1, 0.2, 0.3)
        val t = Vector3()
        val out = t.subtractAndSet(u, v)
        assertNotNull(out)
        assertTrue(out === t)
        assertEquals(1.0, t.x, 0.0)
        assertEquals(2.0, t.y, 0.0)
        assertEquals(3.0, t.z, 0.0)
    }

    @Test
    fun testSubtractAndCreate() {
        val u = Vector3(1.1, 2.2, 3.3)
        val v = Vector3(0.1, 0.2, 0.3)
        val t = Vector3.subtractAndCreate(u, v)
        assertNotNull(t)
        assertEquals(1.0, t.x, 0.0)
        assertEquals(2.0, t.y, 0.0)
        assertEquals(3.0, t.z, 0.0)
    }

    @Test
    fun testMultiplyFromDouble() {
        val v = Vector3(1.0, 2.0, 3.0)
        val out = v.multiply(2.0)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(2.0, v.x, 0.0)
        assertEquals(4.0, v.y, 0.0)
        assertEquals(6.0, v.z, 0.0)
    }

    @Test
    fun testMultiplyFromVector3() {
        val v = Vector3(1.0, 2.0, 3.0)
        val v1 = Vector3(2.0, 3.0, 4.0)
        val out = v.multiply(v1)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(2.0, v.x, 0.0)
        assertEquals(6.0, v.y, 0.0)
        assertEquals(12.0, v.z, 0.0)
    }

    @Test
    fun testMultiplyFomDoubleMatrix() {
        val v = Vector3(1.0, 2.0, 3.0)
        val matrix = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val out = v.multiply(matrix)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(51.0, v.x, 0.0)
        assertEquals(58.0, v.y, 0.0)
        assertEquals(65.0, v.z, 0.0)
    }

    @Test
    fun testMultiplyFromMatrix4() {
        val v = Vector3(1.0, 2.0, 3.0)
        val matrix = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val matrix4 = Matrix4(matrix)
        val out = v.multiply(matrix4)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(51.0, v.x, 0.0)
        assertEquals(58.0, v.y, 0.0)
        assertEquals(65.0, v.z, 0.0)
    }

    @Test
    fun testMultiplyAndSet() {
        val v = Vector3()
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v2 = Vector3(2.0, 3.0, 4.0)
        val out = v.multiplyAndSet(v1, v2)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(2.0, v.x, 0.0)
        assertEquals(6.0, v.y, 0.0)
        assertEquals(12.0, v.z, 0.0)
    }

    @Test
    fun testMultiplyAndCreateFromTwoVector3() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v2 = Vector3(2.0, 3.0, 4.0)
        val v = Vector3.multiplyAndCreate(v1, v2)
        assertNotNull(v)
        assertEquals(2.0, v.x, 0.0)
        assertEquals(6.0, v.y, 0.0)
        assertEquals(12.0, v.z, 0.0)
    }

    @Test
    fun testMultiplyAndCreateFromVector3Double() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v = Vector3.multiplyAndCreate(v1, 2.0)
        assertNotNull(v)
        assertEquals(2.0, v.x, 0.0)
        assertEquals(4.0, v.y, 0.0)
        assertEquals(6.0, v.z, 0.0)
    }

    @Test
    fun testDivideFromDouble() {
        val v = Vector3(1.0, 2.0, 3.0)
        val out = v.divide(2.0)
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(0.5, v.x, 0.0)
        assertEquals(1.0, v.y, 0.0)
        assertEquals(1.5, v.z, 0.0)
    }

    @Test
    fun testDivideFromVector3() {
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(0.5, 0.25, 4.0)
        val out = u.divide(v)
        assertNotNull(out)
        assertTrue(out === u)
        assertEquals(2.0, u.x, 0.0)
        assertEquals(8.0, u.y, 0.0)
        assertEquals(0.75, u.z, 0.0)
    }

    @Test
    fun testDivideAndSet() {
        val t = Vector3()
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(0.5, 0.25, 4.0)
        val out = t.divideAndSet(u, v)
        assertNotNull(out)
        assertTrue(out === t)
        assertEquals(t.x, 2.0, 0.0)
        assertEquals(t.y, 8.0, 0.0)
        assertEquals(t.z, 0.75, 0.0)
    }

    @Test
    fun testScaleAndSet() {
        val t = Vector3()
        val v = Vector3(1.0, 2.0, 3.0)
        val out = t.scaleAndSet(v, 0.5)
        assertNotNull(out)
        assertTrue(out === t)
        assertEquals(0.5, t.x, 0.0)
        assertEquals(1.0, t.y, 0.0)
        assertEquals(1.5, t.z, 0.0)
    }

    @Test
    fun testScaleAndCreate() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v = Vector3.scaleAndCreate(v1, 0.5)
        assertNotNull(v)
        assertEquals(0.5, v.x, 0.0)
        assertEquals(1.0, v.y, 0.0)
        assertEquals(1.5, v.z, 0.0)
    }

    @Test
    fun testRotateBy() {
        run {
            val q = identity
            val v = Vector3(0.0, 0.0, 1.0)
            v.rotateBy(q)

            assertEquals("X", 0.0, v.x, 1e-14)
            assertEquals("Y", 0.0, v.y, 1e-14)
            assertEquals("Z", 1.0, v.z, 1e-14)
        }
        run {
            val q = Quaternion(0.5, 0.5, 0.5, 0.5)
            val v = Vector3(0.0, 0.0, 1.0)
            v.rotateBy(q)

            assertEquals("X", 1.0, v.x, 1e-14)
            assertEquals("Y", 0.0, v.y, 1e-14)
            assertEquals("Z", 0.0, v.z, 1e-14)
        }
    }

    @Test
    fun testRotateX() {
        val v1 = Vector3(Vector3.X)
        val v2 = Vector3(Vector3.Y)
        val v3 = Vector3(Vector3.Z)
        v1.rotateX(Math.PI)
        v2.rotateX(Math.PI)
        v3.rotateX(Math.PI / 2.0)
        assertEquals(Vector3.X.x, v1.x, 0.0)
        assertEquals(Vector3.X.y, v1.y, 0.0)
        assertEquals(Vector3.X.z, v1.z, 0.0)
        assertEquals(Vector3.NEG_Y.x, v2.x, 1e-14)
        assertEquals(Vector3.NEG_Y.y, v2.y, 1e-14)
        assertEquals(Vector3.NEG_Y.z, v2.z, 1e-14)
        assertEquals(Vector3.NEG_Y.x, v3.x, 1e-14)
        assertEquals(Vector3.NEG_Y.y, v3.y, 1e-14)
        assertEquals(Vector3.NEG_Y.z, v3.z, 1e-14)
    }

    @Test
    fun testRotateY() {
        val v1 = Vector3(Vector3.X)
        val v2 = Vector3(Vector3.Y)
        val v3 = Vector3(Vector3.Z)
        v1.rotateY(Math.PI)
        v2.rotateY(Math.PI)
        v3.rotateY(Math.PI / 2.0)
        assertEquals(Vector3.Y.x, v2.x, 0.0)
        assertEquals(Vector3.Y.y, v2.y, 0.0)
        assertEquals(Vector3.Y.z, v2.z, 0.0)
        assertEquals(Vector3.NEG_X.x, v1.x, 1e-14)
        assertEquals(Vector3.NEG_X.y, v1.y, 1e-14)
        assertEquals(Vector3.NEG_X.z, v1.z, 1e-14)
        assertEquals(Vector3.X.x, v3.x, 1e-14)
        assertEquals(Vector3.X.y, v3.y, 1e-14)
        assertEquals(Vector3.X.z, v3.z, 1e-14)
    }

    @Test
    fun testRotateZ() {
        val v1 = Vector3(Vector3.X)
        val v2 = Vector3(Vector3.Y)
        val v3 = Vector3(Vector3.Z)
        v3.rotateZ(Math.PI)
        v1.rotateZ(Math.PI)
        v2.rotateZ(Math.PI / 2.0)
        assertEquals(Vector3.Z.x, v3.x, 0.0)
        assertEquals(Vector3.Z.y, v3.y, 0.0)
        assertEquals(Vector3.Z.z, v3.z, 0.0)
        assertEquals(Vector3.NEG_X.x, v1.x, 1e-14)
        assertEquals(Vector3.NEG_X.y, v1.y, 1e-14)
        assertEquals(Vector3.NEG_X.z, v1.z, 1e-14)
        assertEquals(Vector3.NEG_X.x, v2.x, 1e-14)
        assertEquals(Vector3.NEG_X.y, v2.y, 1e-14)
        assertEquals(Vector3.NEG_X.z, v2.z, 1e-14)
    }

    @Test
    fun testNormalize() {
        val v = Vector3(1.0, 2.0, 3.0)
        val mod = v.normalize()
        assertEquals(3.7416573867739413, mod, 1e-14)
        assertEquals(0.267261241912424, v.x, 1e-14)
        assertEquals(0.534522483824849, v.y, 1e-14)
        assertEquals(0.801783725737273, v.z, 1e-14)

        val v1 = Vector3(1.0, 0.0, 0.0)
        val mod1 = v1.normalize()
        assertEquals(1.0, mod1, 0.0)
        assertEquals(1.0, v1.x, 0.0)
        assertEquals(0.0, v1.y, 0.0)
        assertEquals(0.0, v1.z, 0.0)

        val v2 = Vector3(0.0, 0.0, 0.0)
        val mod2 = v2.normalize()
        assertEquals(0.0, mod2, 0.0)
        assertEquals(0.0, v2.x, 0.0)
        assertEquals(0.0, v2.y, 0.0)
        assertEquals(0.0, v2.z, 0.0)
    }

    @Test
    fun testOrthoNormalizeFromVector3Array() {
        val v1 = Vector3(Vector3.X)
        val v2 = Vector3(Vector3.Y)
        val v3 = Vector3(Vector3.Z)
        v2.multiply(2.0)
        v3.multiply(3.0)
        Vector3.orthoNormalize(arrayOf(v1, v2, v3))
        assertEquals(1.0, v1.x, 0.0)
        assertEquals(0.0, v1.y, 0.0)
        assertEquals(0.0, v1.z, 0.0)
        assertEquals(0.0, v2.x, 0.0)
        assertEquals(1.0, v2.y, 0.0)
        assertEquals(0.0, v2.z, 0.0)
        assertEquals(0.0, v3.x, 0.0)
        assertEquals(0.0, v3.y, 0.0)
        assertEquals(1.0, v3.z, 0.0)
        v1.setAll(1.0, 1.0, 0.0)
        v2.setAll(0.0, 1.0, 1.0)
        v3.setAll(1.0, 0.0, 1.0)
        Vector3.orthoNormalize(arrayOf(v1, v2, v3))
        assertEquals(0.7071067811865475, v1.x, 1e-14)
        assertEquals(0.7071067811865475, v1.y, 1e-14)
        assertEquals(0.0, v1.z, 1e-14)
        assertEquals(-0.4082482904638631, v2.x, 1e-14)
        assertEquals(0.4082482904638631, v2.y, 1e-14)
        assertEquals(0.8164965809277261, v2.z, 1e-14)
        assertEquals(0.5773502691896256, v3.x, 1e-14)
        assertEquals(-0.5773502691896256, v3.y, 1e-14)
        assertEquals(0.5773502691896257, v3.z, 1e-14)
    }

    @Test
    fun testOrthoNormalizeFromTwoVector3() {
        val v1 = Vector3(Vector3.X)
        val v2 = Vector3(Vector3.Y)
        v2.multiply(2.0)
        Vector3.orthoNormalize(v1, v2)
        assertEquals(1.0, v1.x, 0.0)
        assertEquals(0.0, v1.y, 0.0)
        assertEquals(0.0, v1.z, 0.0)
        assertEquals(0.0, v2.x, 0.0)
        assertEquals(1.0, v2.y, 0.0)
        assertEquals(0.0, v2.z, 0.0)
        v1.setAll(1.0, 1.0, 0.0)
        v2.setAll(0.0, 1.0, 1.0)
        Vector3.orthoNormalize(v1, v2)
        assertEquals("v1: $v1 v2: $v2", 0.7071067811865475, v1.x, 1e-14)
        assertEquals(0.7071067811865475, v1.y, 1e-14)
        assertEquals(0.0, v1.z, 1e-14)
        assertEquals(-0.4082482904638631, v2.x, 1e-14)
        assertEquals(0.4082482904638631, v2.y, 1e-14)
        assertEquals(0.8164965809277261, v2.z, 1e-14)
    }

    @Test
    fun testInverse() {
        val v = Vector3(1.0, 2.0, 3.0)
        val out = v.inverse()
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(-1.0, v.x, 0.0)
        assertEquals(-2.0, v.y, 0.0)
        assertEquals(-3.0, v.z, 0.0)
    }

    @Test
    fun testInvertAndCreate() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v = v1.invertAndCreate()
        assertNotNull(v)
        assertEquals(-1.0, v.x, 0.0)
        assertEquals(-2.0, v.y, 0.0)
        assertEquals(-3.0, v.z, 0.0)
    }

    @Test
    fun testLengthFromDoublesXyz() {
        val l = Vector3.length(1.0, 2.0, 3.0)
        assertEquals(3.74165738677394, l, 1e-14)
    }

    @Test
    fun testLengthFromVector3() {
        val v = Vector3(1.0, 2.0, 3.0)
        val l = Vector3.length(v)
        assertEquals(3.74165738677394, l, 1e-14)
    }

    @Test
    fun testLength2FromVector3() {
        val v = Vector3(1.0, 2.0, 3.0)
        val l2 = Vector3.length2(v)
        assertEquals(14.0, l2, 1e-14)
    }

    @Test
    fun testLength2DoublesXyz() {
        val l2 = Vector3.length2(1.0, 2.0, 3.0)
        assertEquals(14.0, l2, 1e-14)
    }

    @Test
    fun testLengthFromSelf() {
        val v = Vector3(1.0, 2.0, 3.0)
        val l = v.length()
        assertEquals(3.74165738677394, l, 1e-14)
    }

    @Test
    fun testLength2FromSelf() {
        val v = Vector3(1.0, 2.0, 3.0)
        val l2 = v.length2()
        assertEquals(14.0, l2, 1e-14)
    }

    @Test
    fun testDistanceToFromVector3() {
        val v1 = Vector3(0.0, 1.0, 2.0)
        val v2 = Vector3(3.0, 5.0, 7.0)
        val distance1 = v1.distanceTo(v2)
        val distance2 = v2.distanceTo(v1)
        assertEquals(7.07106781186548, distance1, 1e-14)
        assertEquals(7.07106781186548, distance2, 1e-14)
    }

    @Test
    fun testDistanceToFromDoublesXyz() {
        val v1 = Vector3(0.0, 1.0, 2.0)
        val v2 = Vector3(3.0, 5.0, 7.0)
        val distance1 = v1.distanceTo(3.0, 5.0, 7.0)
        val distance2 = v2.distanceTo(0.0, 1.0, 2.0)
        assertEquals(7.07106781186548, distance1, 1e-14)
        assertEquals(7.07106781186548, distance2, 1e-14)
    }

    @Test
    fun testDistanceToFromTwoVector3() {
        val v1 = Vector3(0.0, 1.0, 2.0)
        val v2 = Vector3(3.0, 5.0, 7.0)
        val distance1 = Vector3.distanceTo(v1, v2)
        val distance2 = Vector3.distanceTo(v2, v1)
        assertEquals(distance1, 7.07106781186548, 1e-14)
        assertEquals(distance2, 7.07106781186548, 1e-14)
    }

    @Test
    fun testDistanceToFromTwoPointsDoublesXyz() {
        val distance1 = Vector3.distanceTo(0.0, 1.0, 2.0, 3.0, 5.0, 7.0)
        val distance2 = Vector3.distanceTo(3.0, 5.0, 7.0, 0.0, 1.0, 2.0)
        assertEquals(7.07106781186548, distance1, 1e-14)
        assertEquals(7.07106781186548, distance2, 1e-14)
    }

    @Test
    fun testDistanceTo2FromVector3() {
        val v1 = Vector3(0.0, 1.0, 2.0)
        val v2 = Vector3(3.0, 5.0, 7.0)
        val distance1 = v1.distanceTo2(v2)
        val distance2 = v2.distanceTo2(v1)
        assertEquals(50.0, distance1, 0.0)
        assertEquals(50.0, distance2, 0.0)
    }

    @Test
    fun testDistanceTo2FromDoublesXyz() {
        val v1 = Vector3(0.0, 1.0, 2.0)
        val v2 = Vector3(3.0, 5.0, 7.0)
        val distance1 = v1.distanceTo2(3.0, 5.0, 7.0)
        val distance2 = v2.distanceTo2(0.0, 1.0, 2.0)
        assertEquals(50.0, distance1, 0.0)
        assertEquals(50.0, distance2, 0.0)
    }

    @Test
    fun testDistanceTo2FromTwoVector3() {
        val v1 = Vector3(0.0, 1.0, 2.0)
        val v2 = Vector3(3.0, 5.0, 7.0)
        val distance1 = Vector3.distanceTo2(v1, v2)
        val distance2 = Vector3.distanceTo2(v2, v1)
        assertEquals(50.0, distance1, 0.0)
        assertEquals(50.0, distance2, 0.0)
    }

    @Test
    fun testDistanceTo2FromTwoPointsDoublesXyz() {
        val distance1 = Vector3.distanceTo2(0.0, 1.0, 2.0, 3.0, 5.0, 7.0)
        val distance2 = Vector3.distanceTo2(3.0, 5.0, 7.0, 0.0, 1.0, 2.0)
        assertEquals(50.0, distance1, 0.0)
        assertEquals(50.0, distance2, 0.0)
    }

    @Test
    fun testAbsoluteValue() {
        val v = Vector3(-0.0, -1.0, -2.0)
        val out = v.absoluteValue()
        assertNotNull(out)
        assertTrue(out === v)
        assertEquals(0.0, v.x, 0.0)
        assertEquals(1.0, v.y, 0.0)
        assertEquals(2.0, v.z, 0.0)
    }

    @Test
    fun testProjectFromVector3() {
        val a = Vector3(1.0, 1.0, 0.0)
        val b = Vector3(2.0, 0.0, 0.0)
        val v = b.project(a)
        assertNotNull(v)
        assertTrue(v === b)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
    }

    @Test
    fun testProjectFromDoubleArrayMatrix() {
        val m = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        val v = Vector3(2.0, 3.0, 4.0)
        val out = v.project(m)
        assertNotNull(out)
        assertSame(out, v)
        assertEquals(0.5, out.x, 1e-14)
        assertEquals(0.75, out.y, 1e-14)
        assertEquals(1.0, out.z, 1e-14)
    }

    @Test
    fun testProjectFromMatrix4() {
        val m = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        val v = Vector3(2.0, 3.0, 4.0)
        val out = v.project(Matrix4(m))
        assertNotNull(out)
        assertSame(out, v)
        assertEquals(0.5, out.x, 1e-14)
        assertEquals(0.75, out.y, 1e-14)
        assertEquals(1.0, out.z, 1e-14)
    }

    @Test
    fun testProjectAndCreate() {
        val a = Vector3(1.0, 1.0, 0.0)
        val b = Vector3(2.0, 0.0, 0.0)
        val v = Vector3.projectAndCreate(a, b)
        assertNotNull(v)
        assertFalse(v === a)
        assertFalse(v === b)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
    }

    @Test
    fun testAngle() {
        val v1 = Vector3(Vector3.X)
        val v2 = Vector3(Vector3.Y)
        val v = Vector3(1.0, 1.0, 1.0)
        val angle1 = v1.angle(v2)
        val angle2 = v2.angle(v1)
        assertEquals(90.0, angle1, 0.0)
        assertEquals(90.0, angle2, 0.0)
        assertEquals(54.735610317245346, v.angle(Vector3.X), 1e-14)
        assertEquals(54.735610317245346, v.angle(Vector3.Y), 1e-14)
        assertEquals(54.735610317245346, v.angle(Vector3.Z), 1e-14)
    }

    @Test
    fun testDotFromTwoVector3() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v2 = Vector3(4.0, 5.0, 6.0)
        val dot = Vector3.dot(v1, v2)
        assertEquals(32.0, dot, 0.0)
    }

    @Test
    fun testDotFromVector3() {
        val v = Vector3(1.0, 2.0, 3.0)
        val v1 = Vector3(4.0, 5.0, 6.0)
        val dot = v.dot(v1)
        assertEquals(32.0, dot, 0.0)
    }

    @Test
    fun testDotFromDoublesXyz() {
        val v = Vector3(1.0, 2.0, 3.0)
        val dot = v.dot(4.0, 5.0, 6.0)
        assertEquals(32.0, dot, 0.0)
    }

    @Test
    fun testDotFromTwoDoublesXyz() {
        val dot = Vector3.dot(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)
        assertEquals(32.0, dot, 0.0)
    }

    @Test
    fun testCrossFromVector3() {
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(4.0, 5.0, 6.0)
        val out = u.cross(v)
        assertNotNull(out)
        assertTrue(out === u)
        assertEquals(-3.0, u.x, 0.0)
        assertEquals(6.0, u.y, 0.0)
        assertEquals(-3.0, u.z, 0.0)
    }

    @Test
    fun testCrossFromDoublesXyz() {
        val u = Vector3(1.0, 2.0, 3.0)
        val out = u.cross(4.0, 5.0, 6.0)
        assertNotNull(out)
        assertTrue(out === u)
        assertEquals(-3.0, u.x, 0.0)
        assertEquals(6.0, u.y, 0.0)
        assertEquals(-3.0, u.z, 0.0)
    }

    @Test
    fun testCrossAndSet() {
        val t = Vector3()
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(4.0, 5.0, 6.0)
        val out = t.crossAndSet(u, v)
        assertNotNull(out)
        assertTrue(out === t)
        assertEquals(-3.0, t.x, 0.0)
        assertEquals(6.0, t.y, 0.0)
        assertEquals(-3.0, t.z, 0.0)
    }

    @Test
    fun testCrossAndCreate() {
        val u = Vector3(1.0, 2.0, 3.0)
        val v = Vector3(4.0, 5.0, 6.0)
        val t = Vector3.crossAndCreate(u, v)
        assertNotNull(t)
        assertEquals(-3.0, t.x, 0.0)
        assertEquals(6.0, t.y, 0.0)
        assertEquals(-3.0, t.z, 0.0)
    }

    @Test
    fun testGetRotationTo() {
        val out = Vector3.X.getRotationTo(Vector3.Y)
        assertNotNull(out)
        assertEquals(0.7071067811865475, out.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out.y))
        assertEquals(0.7071067811865475, out.z, 1e-14)
        val out1 = Vector3.Y.getRotationTo(Vector3.Z)
        assertNotNull(out1)
        assertEquals(0.7071067811865475, out1.w, 1e-14)
        assertEquals(0.7071067811865475, out1.x, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out1.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out1.z))
        val out2 = Vector3.X.getRotationTo(Vector3.Z)
        assertNotNull(out2)
        assertEquals(0.7071067811865475, out2.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out2.x))
        assertEquals(-0.7071067811865475, out2.y, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out2.z))
        val out3 = Vector3.X.getRotationTo(Vector3.X)
        assertNotNull(out3)
        assertEquals(java.lang.Double.doubleToRawLongBits(1.0), java.lang.Double.doubleToRawLongBits(out3.w))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out3.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out3.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out3.z))
        val out4 = Vector3.X.getRotationTo(Vector3.NEG_X)
        assertNotNull(out4)
        assertEquals(0.0, out4.w, 1e-14)
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out4.x))
        assertEquals(java.lang.Double.doubleToRawLongBits(0.0), java.lang.Double.doubleToRawLongBits(out4.y))
        assertEquals(java.lang.Double.doubleToRawLongBits(-1.0), java.lang.Double.doubleToRawLongBits(out4.z))
    }

    @Test
    fun testLerp() {
        val v = Vector3(1.0, 0.0, 0.0)
        val vp = Vector3(0.0, 1.0, 0.0)
        val v1 = v.lerp(vp, 0.0)
        assertNotNull(v1)
        assertTrue(v1 === v)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
        v.setAll(1.0, 0.0, 0.0)
        val v2 = v.lerp(vp, 1.0)
        assertNotNull(v2)
        assertTrue(v2 === v)
        assertEquals(0.0, v.x, 0.0)
        assertEquals(1.0, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
        v.setAll(1.0, 0.0, 0.0)
        val v3 = v.lerp(vp, 0.5)
        assertNotNull(v3)
        assertTrue(v3 === v)
        assertEquals(0.5, v.x, 0.0)
        assertEquals(0.5, v.y, 0.0)
        assertEquals(0.0, v.z, 0.0)
    }

    @Test
    fun testLerpAndSet() {
        val v = Vector3(1.0, 0.0, 0.0)
        val vp = Vector3(0.0, 1.0, 0.0)
        val out = Vector3()
        val v1 = out.lerpAndSet(v, vp, 0.0)
        assertNotNull(v1)
        assertTrue(v1 === out)
        assertEquals(1.0, v1.x, 0.0)
        assertEquals(0.0, v1.y, 0.0)
        assertEquals(0.0, v1.z, 0.0)
        val v2 = out.lerpAndSet(v, vp, 1.0)
        assertNotNull(v2)
        assertTrue(v2 === out)
        assertEquals(0.0, v2.x, 0.0)
        assertEquals(1.0, v2.y, 0.0)
        assertEquals(0.0, v2.z, 0.0)
        val v3 = out.lerpAndSet(v, vp, 0.5)
        assertNotNull(v3)
        assertTrue(v3 === out)
        assertEquals(0.5, v3.x, 0.0)
        assertEquals(0.5, v3.y, 0.0)
        assertEquals(0.0, v3.z, 0.0)
    }

    @Test
    fun testLerpAndCreate() {
        val v = Vector3(1.0, 0.0, 0.0)
        val vp = Vector3(0.0, 1.0, 0.0)
        val v1 = Vector3.lerpAndCreate(v, vp, 0.0)
        assertNotNull(v1)
        assertEquals(1.0, v1.x, 0.0)
        assertEquals(0.0, v1.y, 0.0)
        assertEquals(0.0, v1.z, 0.0)
        val v2 = Vector3.lerpAndCreate(v, vp, 1.0)
        assertNotNull(v2)
        assertEquals(0.0, v2.x, 0.0)
        assertEquals(1.0, v2.y, 0.0)
        assertEquals(0.0, v2.z, 0.0)
        val v3 = Vector3.lerpAndCreate(v, vp, 0.5)
        assertNotNull(v3)
        assertEquals(0.5, v3.x, 0.0)
        assertEquals(0.5, v3.y, 0.0)
        assertEquals(0.0, v3.z, 0.0)
    }

    @Test
    fun testClone() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v = v1.clone()
        assertNotNull(v)
        assertFalse(v === v1)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(2.0, v.y, 0.0)
        assertEquals(3.0, v.z, 0.0)
    }

    @Test
    fun testIsUnit() {
        assertTrue(Vector3.X.isUnit)
        assertTrue(Vector3.Y.isUnit)
        assertTrue(Vector3.Z.isUnit)
        assertFalse(Vector3(1.0).isUnit)
        assertFalse(Vector3(0.0).isUnit)
    }

    @Test
    fun testIsUnitWithMargin() {
        assertTrue(Vector3.X.isUnit(0.1))
        assertTrue(Vector3.Y.isUnit(0.1))
        assertTrue(Vector3.Z.isUnit(0.1))
        assertFalse(Vector3(1.0).isUnit(0.1))
        assertFalse(Vector3(0.0).isUnit(0.1))
        assertTrue(Vector3(0.95, 0.0, 0.0).isUnit(0.316227766016838))
        assertFalse(Vector3(0.95, 0.0, 0.0).isUnit(0.05))
    }

    @Test
    fun testIsZero() {
        assertFalse(Vector3.X.isZero)
        assertFalse(Vector3.Y.isZero)
        assertFalse(Vector3.Z.isZero)
        assertFalse(Vector3(1.0).isZero)
        assertTrue(Vector3(0.0).isZero)
    }

    @Test
    fun testIsZeroWithMargin() {
        assertFalse(Vector3.X.isZero(0.1))
        assertFalse(Vector3.Y.isZero(0.1))
        assertFalse(Vector3.Z.isZero(0.1))
        assertFalse(Vector3(1.0).isZero(0.1))
        assertTrue(Vector3(0.0).isZero(0.1))
        assertTrue(Vector3(0.1, 0.0, 0.0).isZero(0.1))
    }

    @Test
    fun testGetAxisVector() {
        assertEquals(Vector3.X, Vector3.getAxisVector(Vector3.Axis.X))
        assertEquals(Vector3.Y, Vector3.getAxisVector(Vector3.Axis.Y))
        assertEquals(Vector3.Z, Vector3.getAxisVector(Vector3.Axis.Z))
    }

//    @Test(expected = NullPointerException::class)
//    fun testGetAxisVectorWithNull() {
//        Vector3.getAxisVector(null)
//    }

    @Test
    fun testAxisValueOf() {
        assertEquals(Vector3.Axis.X, Vector3.Axis.valueOf("X"))
        assertEquals(Vector3.Axis.Y, Vector3.Axis.valueOf("Y"))
        assertEquals(Vector3.Axis.Z, Vector3.Axis.valueOf("Z"))
    }

    @Test
    fun testEquals() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v2 = Vector3(1.0, 2.0, 3.0)
        val v3 = Vector3(4.0, 5.0, 6.0)
        val v4 = Vector3(1.0, 5.0, 6.0)
        val v5 = Vector3(1.0, 2.0, 6.0)
        assertTrue(v1 == v2)
        assertFalse(v1 == v3)
        assertFalse(v1 == v4)
        assertFalse(v1 == v5)
//        assertFalse(v1 == "WRONG")
//        assertFalse(v1 == null)
    }

    @Test
    fun testEqualsWithError() {
        val v1 = Vector3(1.0, 2.0, 3.0)
        val v2 = Vector3(1.0, 2.0, 3.0)
        val v3 = Vector3(4.0, 5.0, 6.0)
        val v4 = Vector3(1.0, 5.0, 6.0)
        val v5 = Vector3(1.0, 2.0, 6.0)
        val v6 = Vector3(1.1, 2.0, 3.0)
        val v7 = Vector3(1.1, 2.1, 3.0)
        val v8 = Vector3(1.1, 2.1, 3.1)
        assertTrue(v1.equals(v2, 0.0))
        assertFalse(v1.equals(v3, 1.0))
        assertFalse(v1.equals(v4, 1.0))
        assertFalse(v1.equals(v5, 1.0))
        assertTrue(v1.equals(v6, 0.2))
        assertTrue(v1.equals(v7, 0.2))
        assertTrue(v1.equals(v8, 0.2))
    }

    @Test
    fun testToString() {
        val v = Vector3()
        assertNotNull(v.toString())
    }
}
