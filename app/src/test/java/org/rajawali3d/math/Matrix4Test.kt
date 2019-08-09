package org.rajawali3d.math

import org.junit.Test
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.math.vector.Vector3.Axis

import java.util.Arrays

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue

/**
 * @author Jared Woolston (jwoolston@keywcorp.com)
 */
class Matrix4Test {

    @Test
    fun testConstructorNoArgs() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)

        val m = Matrix4()
        assertNotNull(m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testConstructorFromMatrix4() {
        val expected = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)

        val from = Matrix4(expected)
        assertNotNull(from)
        val m = Matrix4(from)
        assertNotNull(m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testConstructorFromDoubleArray() {
        val expected = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)

        val m = Matrix4(expected)
        assertNotNull(m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testConstructorFromFloatArray() {
        val expected = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f)

        val m = Matrix4(expected)
        assertNotNull(m)
        val result = m.floatValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i].toDouble(), result[i].toDouble(), 1e-14)
        }
    }

    @Test
    fun testConstructorFromQuaternion() {
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val q = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val m = Matrix4(q)
        assertNotNull(m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromMatrix4() {
        val expected = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)

        val from = Matrix4(expected)
        val m = Matrix4()
        val out = m.setAll(from)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromDoubleArray() {
        val expected = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)

        val m = Matrix4()
        val out = m.setAll(expected)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromFloatArray() {
        val from = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f)
        val expected = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)

        val m = Matrix4()
        val out = m.setAll(from)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromQuaternion() {
        val from = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f)
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4(from)
        val q = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val out = m.setAll(q)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromQuaternionComponents() {
        val from = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f)
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4(from)
        val out = m.setAll(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        assertFalse(result == expected)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromAxesAndPosition() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 2.0, 3.0, 4.0, 1.0)
        val position = Vector3(2.0, 3.0, 4.0)
        val forward = Vector3(0.0, 1.0, 1.0)
        val up = Vector3(0.0, 1.0, -1.0)
        val m = Matrix4()
        val out = m.setAll(Vector3.X, up, forward, position)
        assertNotNull(out)
        assertSame(out, m)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetAllFromPositionScaleRotation() {
        val expected = doubleArrayOf(0.6603582554517136, 1.4039252336448595, -0.26724299065420565, 0.0, -0.55803738317757, 1.3932710280373835, 0.4511214953271028, 0.0, 0.5027570093457944, -0.2977570093457944, 0.8515732087227414, 0.0, 2.0, 3.0, 4.0, 1.0)
        val rotation = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val position = Vector3(2.0, 3.0, 4.0)
        val scale = Vector3(1.0, 2.0, 1.0)
        val m = Matrix4()
        val out = m.setAll(position, scale, rotation)
        assertNotNull(out)
        assertSame(out, m)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testIdentity() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)

        val m = Matrix4(from)
        val out = m.identity()
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testZero() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val expected = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        val m = Matrix4(from)
        val out = m.zero()
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testDeterminant() {
        val from = doubleArrayOf(16.0, 2.0, 3.0, 13.0, 5.0, 11.0, 10.0, 8.0, 9.0, 7.0, 6.0, 12.0, 4.0, 8.0, 12.0, 19.0)
        val expected = -3672.0
        val m = Matrix4(from)
        val result = m.determinant()
        assertEquals(expected, result, 1e-14)
    }

    @Test
    fun testInverse() {
        val from = doubleArrayOf(16.0, 5.0, 9.0, 4.0, 2.0, 11.0, 7.0, 8.0, 3.0, 10.0, 6.0, 12.0, 13.0, 8.0, 12.0, 19.0)
        val singular = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        val expected = doubleArrayOf(0.1122004357298475, -0.19281045751633988, 0.22222222222222224, -0.08278867102396514, 0.08088235294117647, -0.0661764705882353, 0.25, -0.14705882352941177, -0.11683006535947714, 0.428921568627451, -0.5833333333333334, 0.2124183006535948, -0.03703703703703704, -0.11111111111111112, 0.11111111111111112, 0.03703703703703704)
        val m = Matrix4(from)
        val out = m.inverse()
        assertNotNull(out)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
        val sing = Matrix4(singular)
        var thrown = false
        try {
            sing.inverse()
        } catch (e: IllegalStateException) {
            thrown = true
        } finally {
            assertTrue(thrown)
        }
    }

    @Test
    fun testInverseMultiplication() {
        /* given that a matrix is invertable, test that:
           - a matrix times it's inverse equals the identity
           - an inverse times it's matrix equals the identity */
        val seed = doubleArrayOf(4.0, 1.0, 1.0, 1.0, 1.0, 4.0, 1.0, 1.0, 1.0, 1.0, 4.0, 1.0, 1.0, 1.0, 1.0, 4.0)
        val expected = Matrix4().identity().doubleValues
        val charm = Matrix4(seed)
        val strange = Matrix4(seed)
        strange.inverse()

        val result1 = charm.clone().multiply(strange).doubleValues
        for (i in expected.indices) {
            assertEquals("matrix times inverse", expected[i], result1[i], 1e-14)
        }

        val result2 = strange.clone().multiply(charm).doubleValues
        for (i in expected.indices) {
            assertEquals("inverse times matrix", expected[i], result2[i], 1e-14)
        }
    }

    @Test
    fun testTranspose() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val expected = doubleArrayOf(1.0, 5.0, 9.0, 13.0, 2.0, 6.0, 10.0, 14.0, 3.0, 7.0, 11.0, 15.0, 4.0, 8.0, 12.0, 16.0)
        val m = Matrix4(from)
        val out = m.transpose()
        assertNotNull(out)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testAdd() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val add = doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val expected = doubleArrayOf(2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0)
        val fromM = Matrix4(from)
        val addM = Matrix4(add)
        val out = fromM.add(addM)
        assertNotNull(out)
        assertEquals(fromM, out)
        val result = fromM.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSubtract() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val subtract = doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val expected = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0)
        val fromM = Matrix4(from)
        val subtractM = Matrix4(subtract)
        val out = fromM.subtract(subtractM)
        assertNotNull(out)
        assertEquals(fromM, out)
        val result = fromM.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testMultiply() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val multiply = doubleArrayOf(15.0, 14.0, 13.0, 12.0, 11.0, 10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0)
        val expected = doubleArrayOf(358.0, 412.0, 466.0, 520.0, 246.0, 284.0, 322.0, 360.0, 134.0, 156.0, 178.0, 200.0, 22.0, 28.0, 34.0, 40.0)
        val fromM = Matrix4(from)
        val multiplyM = Matrix4(multiply)
        val out = fromM.multiply(multiplyM)
        assertNotNull(out)
        assertEquals(fromM, out)
        val result = fromM.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Index " + i + " Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testLeftMultiply() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val multiply = doubleArrayOf(15.0, 14.0, 13.0, 12.0, 11.0, 10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0)
        val expected = doubleArrayOf(70.0, 60.0, 50.0, 40.0, 214.0, 188.0, 162.0, 136.0, 358.0, 316.0, 274.0, 232.0, 502.0, 444.0, 386.0, 328.0)
        val fromM = Matrix4(from)
        val multiplyM = Matrix4(multiply)
        val out = fromM.leftMultiply(multiplyM)
        assertNotNull(out)
        assertEquals(fromM, out)
        val result = fromM.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Index " + i + " Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testMultiplyDouble() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val factor = 2.0
        val expected = doubleArrayOf(2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0)
        val fromM = Matrix4(from)
        val out = fromM.multiply(factor)
        assertNotNull(out)
        assertEquals(fromM, out)
        val result = fromM.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testTranslateWithVector3() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 1.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 1.0, 0.0, // Col 2
                1.0, 2.0, 3.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.identity()
        val out = m.translate(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testTranslateFromDoubles() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 1.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 1.0, 0.0, // Col 2
                1.0, 2.0, 3.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.identity()
        val out = m.translate(1.0, 2.0, 3.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testNegTranslate() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 1.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 1.0, 0.0, // Col 2
                -1.0, -2.0, -3.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.identity()
        val out = m.negTranslate(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testScaleFromVector3() {
        val expected = doubleArrayOf(2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        m.identity()
        val out = m.scale(Vector3(2.0, 3.0, 4.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testScaleFromDoubles() {
        val expected = doubleArrayOf(2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        m.identity()
        val out = m.scale(2.0, 3.0, 4.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testScaleFromDouble() {
        val expected = doubleArrayOf(2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        m.identity()
        val out = m.scale(2.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testRotateWithQuaternion() {
        val e = Matrix4()
        var expected: DoubleArray
        val m = Matrix4()
        var result: DoubleArray
        // Test X Axis
        m.rotate(Quaternion(Vector3.X, 20.0))
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.X, 20.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("X - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Y
        m.identity()
        m.rotate(Quaternion(Vector3.Y, 30.0))
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Y, 30.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Y - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Z
        m.identity()
        m.rotate(Quaternion(Vector3.Z, 40.0))
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Z, 40.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Z - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testRotateWithVector3AxisAngle() {
        val e = Matrix4()
        var expected: DoubleArray
        val m = Matrix4()
        var result: DoubleArray
        // Test X Axis
        m.rotate(Vector3.X, 20.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.X, 20.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("X - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Y
        m.identity()
        m.rotate(Vector3.Y, 30.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Y, 30.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Y - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Z
        m.identity()
        m.rotate(Vector3.Z, 40.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Z, 40.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Z - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testRotateWithAxisAngle() {
        val e = Matrix4()
        var expected: DoubleArray
        val m = Matrix4()
        var result: DoubleArray
        // Test X Axis
        m.rotate(Axis.X, 20.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.X, 20.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("X - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Y
        m.identity()
        m.rotate(Axis.Y, 30.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Y, 30.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Y - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Z
        m.identity()
        m.rotate(Axis.Z, 40.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Z, 40.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Z - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testRotateDoubleAxisAngle() {
        val e = Matrix4()
        var expected: DoubleArray
        val m = Matrix4()
        var result: DoubleArray
        // Test X Axis
        m.rotate(1.0, 0.0, 0.0, 20.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.X, 20.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("X - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Y
        m.identity()
        m.rotate(0.0, 1.0, 0.0, 30.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Y, 30.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Y - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
        // Test Z
        m.identity()
        m.rotate(0.0, 0.0, 1.0, 40.0)
        result = m.doubleValues
        assertNotNull(result)
        e.setAll(Quaternion(Vector3.Z, 40.0))
        expected = e.doubleValues
        for (i in expected.indices) {
            assertEquals("Z - Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testRotateBetweenTwoVectors() {
        val expected = doubleArrayOf(0.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val q = Quaternion()
        q.fromRotationBetween(Vector3.X, Vector3.Y)
        val out = Matrix4(q)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetTranslationFromVector3() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 1.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 1.0, 0.0, // Col 2
                1.0, 2.0, 3.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.identity()
        val out = m.setTranslation(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetTranslationFromDoubles() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 1.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 1.0, 0.0, // Col 2
                1.0, 2.0, 3.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.identity()
        val out = m.setTranslation(1.0, 2.0, 3.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetCoordinateZoom() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 2.0)
        val m = Matrix4()
        m.identity()
        val out = m.setCoordinateZoom(2.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testRotateVector() {
        val m = Matrix4(Quaternion(Vector3.X, 45.0))
        val v = Vector3(0.0, 1.0, 0.0)
        m.rotateVector(v)
        assertEquals(0.0, v.x, 1e-14)
        assertEquals(0.7071067811865475, v.y, 1e-14)
        assertEquals(-0.7071067811865475, v.z, 1e-14)
    }

    @Test
    fun testProjectVector() {
        val m = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        val v = Vector3(2.0, 3.0, 4.0)
        val matrix = Matrix4(m)
        val out = matrix.projectVector(v)
        assertNotNull(out)
        assertSame(out, v)
        assertEquals(0.5, out.x, 1e-14)
        assertEquals(0.75, out.y, 1e-14)
        assertEquals(1.0, out.z, 1e-14)
    }

    @Test
    fun testProjectAndCreateVector() {
        val m = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        val v = Vector3(2.0, 3.0, 4.0)
        val matrix = Matrix4(m)
        val out = matrix.projectAndCreateVector(v)
        assertNotNull(out)
        assertTrue(out !== v)
        assertEquals(0.5, out.x, 1e-14)
        assertEquals(0.75, out.y, 1e-14)
        assertEquals(1.0, out.z, 1e-14)
    }

    @Test
    fun testLerp() {
        val expected = doubleArrayOf(0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5)
        val zero = Matrix4()
        zero.zero()
        val one = Matrix4()
        one.setAll(doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0))
        val out = zero.lerp(one, 0.5)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToNormalMatrix() {
        val from = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 2.0, 3.0, 4.0, 1.0)
        val expected = doubleArrayOf(0.660211240510574, 0.7018151895155996, -0.2670828890740923, 0.0, -0.5578276574045106, 0.6965042017970164, 0.45110187226905063, 0.0, 0.5026988507104198, -0.14872805483576382, 0.851508961743878, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4(from)
        val out = m.setToNormalMatrix()
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToPerspective() {
        val expected = doubleArrayOf(1.3323467750529825, 0.0, 0.0, 0.0, 0.0, 1.7320508075688774, 0.0, 0.0, 0.0, 0.0, -1.002002002002002, -1.0, 0.0, 0.0, -2.002002002002002, 0.0)
        val m = Matrix4()
        val out = m.setToPerspective(1.0, 1000.0, 60.0, 1.3)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToOrthographic2D() {
        val expected = doubleArrayOf(0.001953125, 0.0, 0.0, 0.0, 0.0, 0.00390625, 0.0, 0.0, 0.0, 0.0, -2.0, 0.0, -1.0, -1.0, -1.0, 1.0)
        val m = Matrix4()
        val out = m.setToOrthographic2D(0.0, 0.0, 1024.0, 512.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToOrthographic2D1() {
        val expected = doubleArrayOf(0.001953125, 0.0, 0.0, 0.0, 0.0, 0.00390625, 0.0, 0.0, 0.0, 0.0, -0.20202020202020202, 0.0, -1.0, -1.0, -1.02020202020202, 1.0)
        val m = Matrix4()
        val out = m.setToOrthographic2D(0.0, 0.0, 1024.0, 512.0, 0.1, 10.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToOrthographic() {
        val expected = doubleArrayOf(2.0, 0.0, 0.0, 0.0, 0.0, 1.25, 0.0, 0.0, 0.0, 0.0, -2.5, 0.0, -0.0, -0.0, -1.25, 1.0)
        val m = Matrix4()
        val out = m.setToOrthographic(-0.5, 0.5, -0.8, 0.8, 0.1, 0.9)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result) + " Expected: " + Arrays.toString(expected),
                    expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToTranslationFromVector3() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 2.0, 3.0, 1.0)
        val m = Matrix4()
        m.zero()
        val out = m.setToTranslation(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToTranslationFromDoubles() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 1.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 1.0, 0.0, // Col 2
                1.0, 2.0, 3.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.zero()
        val out = m.setToTranslation(1.0, 2.0, 3.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToScaleFromVector3() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        m.zero()
        val out = m.setToScale(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToScaleFromDoubles() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        m.zero()
        val out = m.setToScale(1.0, 2.0, 3.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToTranslationAndScalingFromVector3s() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 2.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 3.0, 0.0, // Col 2
                3.0, 2.0, 1.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.zero()
        val out = m.setToTranslationAndScaling(Vector3(3.0, 2.0, 1.0), Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToTranslationAndScalingFromDoubles() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 2.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 3.0, 0.0, // Col 2
                3.0, 2.0, 1.0, 1.0 // Col 3
        )
        val m = Matrix4()
        m.zero()
        val out = m.setToTranslationAndScaling(3.0, 2.0, 1.0, 1.0, 2.0, 3.0)
        assertNotNull(out)
        assertTrue(out === m)
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToRotationVector3AxisAngle() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865475, -0.7071067811865476, 0.0, 0.0, 0.7071067811865476, 0.7071067811865475, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        val out = m.setToRotation(Vector3.X, 45.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToRotationAxisAngle() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865475, -0.7071067811865476, 0.0, 0.0, 0.7071067811865476, 0.7071067811865475, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        val out = m.setToRotation(Axis.X, 45.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToRotationDoublesAxisAngle() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865475, -0.7071067811865476, 0.0, 0.0, 0.7071067811865476, 0.7071067811865475, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        val out = m.setToRotation(1.0, 0.0, 0.0, 45.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToRotationTwoVector3() {
        val expected = doubleArrayOf(0.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        val v1 = Vector3(1.0, 0.0, 0.0)
        val v2 = Vector3(0.0, 1.0, 0.0)
        val out = m.setToRotation(v1, v2)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToRotationTwoVectorsDoubles() {
        val expected = doubleArrayOf(0.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        val out = m.setToRotation(1.0, 0.0, 0.0, 0.0, 1.0, 0.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToRotationEulerAngles() {
        val expected = doubleArrayOf(0.8825641192593856, -0.44096961052988237, 0.1631759111665348, 0.0, 0.4698463103929541, 0.8137976813493737, -0.34202014332566866, 0.0, 0.018028311236297265, 0.37852230636979245, 0.9254165783983234, 0.0, 0.0, 0.0, 0.0, 1.0)
        val m = Matrix4()
        val out = m.setToRotation(10.0, 20.0, 30.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToLookAtDirectionUp() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val lookAt = Vector3.subtractAndCreate(Vector3(0.0, 10.0, 10.0), Vector3.ZERO)
        q.lookAt(lookAt, Vector3.Y)
        val expected = q.toRotationMatrix().doubleValues
        val m = Matrix4()
        val out = m.setToLookAt(lookAt, Vector3.Y)
        assertNotNull(out)
        assertSame(out, m)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToLookAtPositionTargetUp() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val lookAt = Vector3.subtractAndCreate(Vector3(0.0, 10.0, 10.0), Vector3.ZERO)
        q.lookAt(lookAt, Vector3.Y)
        val expected = q.toRotationMatrix().doubleValues
        val m = Matrix4()
        val out = m.setToLookAt(Vector3.ZERO, Vector3(0.0, 10.0, 10.0), Vector3.Y)
        assertNotNull(out)
        assertSame(out, m)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testSetToWorld() {
        val expected = doubleArrayOf(-1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865476, -0.7071067811865476, 0.0, 0.0, -0.7071067811865475, -0.7071067811865475, 0.0, 2.0, 3.0, 4.0, 1.0)
        val position = Vector3(2.0, 3.0, 4.0)
        val forward = Vector3(0.0, 1.0, 1.0)
        val up = Vector3(0.0, 1.0, -1.0)
        val m = Matrix4()
        val out = m.setToWorld(position, forward, up)
        assertNotNull(out)
        assertSame(out, m)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testGetTranslation() {
        val from = doubleArrayOf(1.0, 0.0, 0.0, 0.0, // Col 0
                0.0, 2.0, 0.0, 0.0, // Col 1
                0.0, 0.0, 3.0, 0.0, // Col 2
                1.0, 2.0, 3.0, 1.0 // Col 3
        )
        val expected = Vector3(1.0, 2.0, 3.0)
        val m = Matrix4(from)
        val out = m.translation
        assertNotNull(out)
        assertTrue(expected.equals(out, 1e-14))
    }

    @Test
    fun testGetScalingNoArgs() {
        val from = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val expected = Vector3(1.0, 2.0, 3.0)
        val m = Matrix4(from)
        val out = m.scaling
        assertNotNull(out)
        assertTrue(expected.equals(out, 1e-14))
    }

    @Test
    fun testGetScalingInVector3() {
        val from = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val expected = Vector3(1.0, 2.0, 3.0)
        val setIn = Vector3(0.0, 0.0, 0.0)
        val m = Matrix4(from)
        val out = m.getScaling(setIn)
        assertNotNull(out)
        assertTrue(out === setIn)
        assertTrue(expected.equals(out, 1e-14))
    }

    @Test
    fun testCreateRotationMatrixFromQuaternion() {
        val expected = doubleArrayOf(0.6603582554517136, 0.7019626168224298, -0.26724299065420565, 0.0, -0.55803738317757, 0.6966355140186917, 0.4511214953271028, 0.0, 0.5027570093457944, -0.1488785046728972, 0.8515732087227414, 0.0, 0.0, 0.0, 0.0, 1.0)
        val q = Quaternion(0.8958236433584459, -0.16744367165578425,
                -0.2148860452915898, -0.3516317104771469)
        val out = Matrix4.createRotationMatrix(q)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateRotationMatrixVector3AxisAngle() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865475, -0.7071067811865476, 0.0, 0.0, 0.7071067811865476, 0.7071067811865475, 0.0, 0.0, 0.0, 0.0, 1.0)
        val out = Matrix4.createRotationMatrix(Vector3.X, 45.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateRotationMatrixAxisAngle() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865475, -0.7071067811865476, 0.0, 0.0, 0.7071067811865476, 0.7071067811865475, 0.0, 0.0, 0.0, 0.0, 1.0)
        val out = Matrix4.createRotationMatrix(Axis.X, 45.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateRotationMatrixDoublesAxisAngle() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.7071067811865475, -0.7071067811865476, 0.0, 0.0, 0.7071067811865476, 0.7071067811865475, 0.0, 0.0, 0.0, 0.0, 1.0)
        val out = Matrix4.createRotationMatrix(1.0, 0.0, 0.0, 45.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateRotationMatrixEulerAngles() {
        val expected = doubleArrayOf(0.8825641192593856, -0.44096961052988237, 0.1631759111665348, 0.0, 0.4698463103929541, 0.8137976813493737, -0.34202014332566866, 0.0, 0.018028311236297265, 0.37852230636979245, 0.9254165783983234, 0.0, 0.0, 0.0, 0.0, 1.0)
        val out = Matrix4.createRotationMatrix(10.0, 20.0, 30.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals("Result: " + Arrays.toString(result), expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateTranslationMatrixVector3() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 2.0, 3.0, 1.0)
        val out = Matrix4.createTranslationMatrix(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateTranslationMatrixDoubles() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 2.0, 3.0, 1.0)
        val out = Matrix4.createTranslationMatrix(1.0, 2.0, 3.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateScaleMatrixVector3() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val out = Matrix4.createScaleMatrix(Vector3(1.0, 2.0, 3.0))
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testCreateScaleMatrixDoubles() {
        val expected = doubleArrayOf(2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        val out = Matrix4.createScaleMatrix(2.0, 3.0, 4.0)
        assertNotNull(out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testGetFloatValues() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)

        val m = Matrix4()
        val result = m.floatValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i].toDouble(), 1e-14)
        }
    }

    @Test
    fun testGetDoubleValues() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)

        val m = Matrix4()
        val result = m.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testClone() {
        val expected = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)

        val from = Matrix4(expected)
        val out = from.clone()
        assertNotNull(out)
        assertTrue(from !== out)
        val result = out.doubleValues
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testToArray() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)

        val result = DoubleArray(16)
        val m = Matrix4()
        m.toArray(result)
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i], 1e-14)
        }
    }

    @Test
    fun testToFloatArray() {
        val expected = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)

        val result = FloatArray(16)
        val m = Matrix4()
        m.toFloatArray(result)
        assertNotNull(result)
        for (i in expected.indices) {
            assertEquals(expected[i], result[i].toDouble(), 1e-14)
        }
    }

    @Test
    fun testEquals() {
        val from = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
        val a = Matrix4()
        val b = Matrix4()
        val c = Matrix4(from)
        assertEquals(a, b)
        assertNotEquals(a, c)
        assertNotEquals(b, c)
        assertNotEquals(null, a)
        assertNotEquals("not a matrix", a)
    }

    @Test
    fun testToString() {
        val m = Matrix4()
        assertNotNull(m.toString())
    }
}
