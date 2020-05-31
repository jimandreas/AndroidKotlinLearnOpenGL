@file:Suppress("unused")
package org.rajawali3d.math.util

import java.nio.*

/**
 * A collection of methods for working with primitive arrays.
 *
 * @author Jared Woolston (jwoolston@tenkiv.com)
 */
object ArrayUtils {

    /**
     * Converts an array of doubles to an array of floats, using the provided output array.
     *
     * @param input double[] array to be converted.
     * @param output float[] array to store the result in.
     * @return float[] a reference to output. Returned for convenience.
     */
    fun convertDoublesToFloats(input: DoubleArray?, output: FloatArray?): FloatArray? {
        if (input == null || output == null) return output
        for (i in input.indices) {
            output[i] = input[i].toFloat()
        }
        return output
    }

    /**
     * Converts an array of doubles to an array of floats, allocating a new array.
     *
     * @param input double[] array to be converted.
     * @return float[] array with the result. Will be null if input was null.
     */
    fun convertDoublesToFloats(input: DoubleArray?): FloatArray? {
        if (input == null) return null
        val output = FloatArray(input.size)
        for (i in input.indices) {
            output[i] = input[i].toFloat()
        }
        return output
    }

    /**
     * Converts an array of floats to an array of doubles, using the provided output array.
     *
     * @param input float[] array to be converted.
     * @param output double[] array to store the result in.
     * @return float[] a reference to output. Returned for convenience.
     */
    fun convertFloatsToDoubles(input: FloatArray?, output: DoubleArray?): DoubleArray? {
        if (input == null || output == null) return output
        for (i in input.indices) {
            output[i] = input[i].toDouble()
        }
        return output
    }

    /**
     * Converts an array of floats to an array of doubles, allocating a new array.
     *
     * @param input double[] array to be converted.
     * @return float[] array with the result. Will be null if input was null.
     */
    fun convertFloatsToDoubles(input: FloatArray?): DoubleArray? {
        if (input == null) return null
        val output = DoubleArray(input.size)
        for (i in input.indices) {
            output[i] = input[i].toDouble()
        }
        return output
    }

    /**
     * Concatenates a list of double arrays into a single array.
     *
     * @param arrays The arrays.
     * @return The concatenated array.
     *
     * @see {@link http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java}
     */
    fun concatAllDouble(vararg arrays: DoubleArray): DoubleArray {
        var totalLength = 0
        val subArrayCount = arrays.size
        for (i in 0 until subArrayCount) {
            totalLength += arrays[i].size
        }
        val result = arrays[0].copyOf(totalLength)
        var offset = arrays[0].size
        for (i in 1 until subArrayCount) {
            System.arraycopy(arrays[i], 0, result, offset, arrays[i].size)
            offset += arrays[i].size
        }
        return result
    }

    /**
     * Concatenates a list of float arrays into a single array.
     *
     * @param arrays The arrays.
     * @return The concatenated array.
     *
     * @see {@link http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java}
     */
    fun concatAllFloat(vararg arrays: FloatArray): FloatArray {
        var totalLength = 0
        val subArrayCount = arrays.size
        for (i in 0 until subArrayCount) {
            totalLength += arrays[i].size
        }
        val result = arrays[0].copyOf(totalLength)
        var offset = arrays[0].size
        for (i in 1 until subArrayCount) {
            System.arraycopy(arrays[i], 0, result, offset, arrays[i].size)
            offset += arrays[i].size
        }
        return result
    }

    /**
     * Concatenates a list of int arrays into a single array.
     *
     * @param arrays The arrays.
     * @return The concatenated array.
     *
     * @see {@link http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java}
     */
    fun concatAllInt(vararg arrays: IntArray): IntArray {
        var totalLength = 0
        val subArrayCount = arrays.size
        for (i in 0 until subArrayCount) {
            totalLength += arrays[i].size
        }
        val result = arrays[0].copyOf(totalLength)
        var offset = arrays[0].size
        for (i in 1 until subArrayCount) {
            System.arraycopy(arrays[i], 0, result, offset, arrays[i].size)
            offset += arrays[i].size
        }
        return result
    }

    /**
     * Creates a double array from the provided [DoubleBuffer].
     *
     * @param buffer [DoubleBuffer] the data source.
     * @return double array containing the data of the buffer.
     */
    fun getDoubleArrayFromBuffer(buffer: DoubleBuffer): DoubleArray? {
        val array: DoubleArray?
        if (buffer.hasArray()) {
            array = buffer.array()
        } else {
            buffer.rewind()
            array = DoubleArray(buffer.capacity())
            buffer.get(array)
        }
        return array
    }

    /**
     * Creates a float array from the provided [FloatBuffer].
     *
     * @param buffer [FloatBuffer] the data source.
     * @return float array containing the data of the buffer.
     */
    fun getFloatArrayFromBuffer(buffer: FloatBuffer): FloatArray? {
        val array: FloatArray?
        if (buffer.hasArray()) {
            array = buffer.array()
        } else {
            buffer.rewind()
            array = FloatArray(buffer.capacity())
            buffer.get(array)
        }
        return array
    }

    /**
     * Creates an int array from the provided [IntBuffer] or [ShortBuffer].
     *
     * @param buffer [Buffer] the data source. Should be either a [IntBuffer]
     * or [ShortBuffer].
     * @return int array containing the data of the buffer.
     */
    fun getIntArrayFromBuffer(buffer: Buffer): IntArray {
        val array: IntArray?
        if (buffer.hasArray()) {
            array = buffer.array() as IntArray
        } else {
            buffer.rewind()
            array = IntArray(buffer.capacity())
            if (buffer is IntBuffer) {
                buffer.get(array)
            } else if (buffer is ShortBuffer) {
                var count = 0
                while (buffer.hasRemaining()) {
                    array[count] = buffer.get().toInt()
                    ++count
                }
            }
        }
        return array
    }
}
