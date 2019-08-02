/*
 * Copyright (C) 2016-2018 James Andreas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

@file:Suppress("unused")

package com.androidkotlin.opengl.util

import timber.log.Timber
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class GraphicsUtils {
/*
Reference:
https://www.opengl.org/wiki/Calculating_a_Surface_Normal

Algorithm
A surface normal for a triangle can be calculated by taking the vector cross product of two edges of that triangle. The order of the vertices used in the calculation will affect the direction of the normal (in or out of the face w.r.t. winding).

So for a triangle p1, p2, p3, if the vector u = p2 - p1 and the vector v = p3 - p1 then the normal n = u X v and can be calculated by:

Nx = UyVz - UzVy

Ny = UzVx - UxVz

Nz = UxVy - UyVx

Pseudo-code
Given that a vector is a structure composed of three floating point numbers and a Triangle is a structure composed of three Vectors, based on the above definitions:

Begin Function CalculateSurfaceNormal (Input Triangle) Returns Vector

	Set Vector u to (Triangle.p2 minus Triangle.p1)
	Set Vector v to (Triangle.p3 minus Triangle.p1)

	Set Normal.x to (multiply u.y by v.z) minus (multiply u.z by v.y)
	Set Normal.y to (multiply u.z by v.x) minus (multiply u.x by v.z)
	Set Normal.z to (multiply u.x by v.y) minus (multiply u.y by v.x)

	Returning Normal

End Function
*/


    /*
     * Shaders
     */

    /*
     * note - I remove the square function on the illumination fall-off
     * Things were just too dark.   Look for the following term commented out.  - jim a
     *  "* distance"
     *  Also upped the diffuse to 0.6 as a base.
     */
// Vertex Shader from lesson 2
//    does all the lighting in the vertex shader - fragment shader just passes through
//  the color calculation
    // A constant representing the combined model/view/projection matrix.
// A constant representing the combined model/view matrix.
// The position of the light in eye space.
// Per-vertex position information we will pass in.
// Per-vertex color information we will pass in.
// Per-vertex normal information we will pass in.
// This will be passed into the fragment shader.
// The entry point for our vertex shader.
// Transform the vertex into eye space.
// Transform the normal's orientation into eye space.
// Will be used for attenuation.
// Get a lighting direction vector from the light to the vertex.
// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
// pointing in the same direction then it will get max illumination. *** was 0.1, now *** 0.6
// Attenuate the light based on distance.
//  + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance )));  \n"
// HACK: minimal level of diffuse for ambient
// result - blew out the highlihts, didn't bring up the shadows as expected
//   + "   diffuse = min(diffuse, 0.2);  \n"
// Multiply the color by the illumination level. It will be interpolated across the triangle.
// gl_Position is a special variable used to store the final position.
// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    val vertexShaderLesson2: String
        get() = ("uniform mat4 u_MVPMatrix;      \n"
                + "uniform mat4 u_MVMatrix;       \n"
                + "uniform vec3 u_LightPos;       \n"

                + "attribute vec4 a_Position;     \n"
                + "attribute vec4 a_Color;        \n"
                + "attribute vec3 a_Normal;       \n"

                + "varying vec4 v_Color;          \n"

                + "void main()                    \n"
                + "{                              \n"
                + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
                + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
                + "   float distance = length(u_LightPos - modelViewVertex);             \n"
                + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
                + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.6);       \n"
                + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance /* * distance */)));  \n"
                + "   v_Color = a_Color * diffuse;                                       \n"
                + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                + "}                                                                     \n")

    // Set the default precision to medium. We don't need as high of a
    // precision in the fragment shader.
    // This is the color from the vertex shader interpolated across the
    // triangle per fragment.
    // The entry point for our fragment shader.
    // Pass the color directly through the pipeline.
    val fragmentShaderLesson2: String
        get() = ("precision mediump float;       \n"
                + "varying vec4 v_Color;          \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_FragColor = v_Color;     \n"
                + "}                              \n")

    // Define our per-pixel lighting shader.
    // A constant representing the combined model/view/projection matrix.
    // A constant representing the combined model/view matrix.
    // Per-vertex position information we will pass in.
    // Per-vertex color information we will pass in.
    // Per-vertex normal information we will pass in.
    // This will be passed into the fragment shader.
    // This will be passed into the fragment shader.
    // This will be passed into the fragment shader.
    // The entry point for our vertex shader.
    // Transform the vertex into eye space.
    // Pass through the color.
    // Transform the normal's orientation into eye space.
    // gl_Position is a special variable used to store the final position.
    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    val vertexShaderLesson3: String
        get() = ("uniform mat4 u_MVPMatrix;      \n"
                + "uniform mat4 u_MVMatrix;       \n"

                + "attribute vec4 a_Position;     \n"
                + "attribute vec4 a_Color;        \n"
                + "attribute vec3 a_Normal;       \n"

                + "varying vec3 v_Position;       \n"
                + "varying vec4 v_Color;          \n"
                + "varying vec3 v_Normal;         \n"
                + "void main()                                                \n"
                + "{                                                          \n"
                + "   v_Position = vec3(u_MVMatrix * a_Position);             \n"
                + "   v_Color = a_Color;                                      \n"
                + "   v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));      \n"
                + "   gl_Position = u_MVPMatrix * a_Position;                 \n"
                + "}                                                          \n")

    // Set the default precision to medium. We don't need as high of a
    // precision in the fragment shader.
    // The position of the light in eye space.
    // Interpolated position for this fragment.
    // This is the color from the vertex shader interpolated across the
    // triangle per fragment.
    // Interpolated normal for this fragment.
    // The entry point for our fragment shader.
    // Will be used for attenuation.
    // Get a lighting direction vector from the light to the vertex.
    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.  *** was 0.1, now *** 0.6
    // .4 is too bright
    // Add attenuation.
    // Multiply the color by the diffuse illumination level to get final output color.
    val fragmentShaderLesson3: String
        get() = ("precision mediump float;       \n"
                + "uniform vec3 u_LightPos;       \n"

                + "varying vec3 v_Position;		\n"
                + "varying vec4 v_Color;          \n"
                + "varying vec3 v_Normal;         \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   float distance = length(u_LightPos - v_Position);                  \n"
                + "   vec3 lightVector = normalize(u_LightPos - v_Position);             \n"
                + "   float diffuse = max(dot(v_Normal, lightVector), 0.001);              \n"
                + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance )));  \n"

                + "   diffuse = diffuse * 3.0;  \n"
                + "   gl_FragColor = v_Color * diffuse;                                  \n"
                + "}                                                                     \n")

    companion object {

        data class AtomDesc(
                val symbol: String = "",
                var xCoor: Double = 0.0,
                var yCoor: Double = 0.0,
                var zCoor: Double = 0.0)

        private val u = FloatArray(3)
        private val v = FloatArray(3)
        private val n = FloatArray(3)
        private val t = FloatArray(3)

        // https://www.opengl.org/wiki/Calculating_a_Surface_Normal
        fun getNormal(p1: FloatArray, p2: FloatArray, p3: FloatArray): FloatArray {
            u[0] = p2[0] - p1[0]
            u[1] = p2[1] - p1[1]
            u[2] = p2[2] - p1[2]

            v[0] = p3[0] - p1[0]
            v[1] = p3[1] - p1[1]
            v[2] = p3[2] - p1[2]

            n[0] = u[1] * v[2] - u[2] * v[1]
            n[1] = u[2] * v[0] - u[0] * v[2]
            n[2] = u[0] * v[1] - u[1] * v[0]
            return normalize(n)
        }

        private fun normalize(p1: FloatArray): FloatArray {
            val mag = sqrt((p1[0] * p1[0] +
                    p1[1] * p1[1] +
                    p1[2] * p1[2]).toDouble()).toFloat()
            t[0] = p1[0] / mag
            t[1] = p1[1] / mag
            t[2] = p1[2] / mag
            return t
        }


        fun centerMolecule(mol: MutableList<AtomDesc>) {

            var maxX = 0.0
            var maxY = 0.0
            var maxZ = 0.0

            var minX = Double.MAX_VALUE
            var minY = Double.MAX_VALUE
            var minZ = Double.MAX_VALUE
            
            var iterator = mol.iterator()
            var thismol : AtomDesc
            while (iterator.hasNext()) {
                thismol = iterator.next()
                
                maxX = max(maxX, thismol.xCoor)
                minX = min(minX, thismol.xCoor)

                maxY = max(maxY, thismol.yCoor)
                minY = min(minY, thismol.yCoor)

                maxZ = max(maxZ, thismol.zCoor)
                minZ = min(minZ, thismol.zCoor)
            }

            val centerX = (maxX - minX) / 2f + minX
            val centerY = (maxY - minY) / 2f + minY
            val centerZ = (maxZ - minZ) / 2f + minZ

            iterator = mol.iterator()
            while (iterator.hasNext()) {
                thismol = iterator.next()

                thismol.xCoor -= centerX
                thismol.yCoor -= centerY
                thismol.zCoor -= centerZ
            }

            val dcOffsetX = maxX - minX
            val dcOffsetY = maxY - minY
            val dcOffsetZ = maxZ - minZ

            val dcOffset = sqrt((dcOffsetX * dcOffsetX + dcOffsetY * dcOffsetY + dcOffsetZ + dcOffsetZ).toDouble()).toFloat()
            Timber.i("DCoffset is %5.2f", dcOffset)
        }
    }
}