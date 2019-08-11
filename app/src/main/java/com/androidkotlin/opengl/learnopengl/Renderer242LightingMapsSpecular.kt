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
 * Translation to kotlin and adaptation to android architecture:
 * Jim Andreas  jim@jimandreas.com
 */
package com.androidkotlin.opengl.learnopengl

// translation of
//   LearnOpenGL-master\src\2.lighting\4.2.lighting_maps_specular_map

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView

import com.androidkotlin.opengl.ui.ViewModel
import com.androidkotlin.opengl.util.*

import com.androidkotlin.opengl.realtime.RendererBaseClass
import org.rajawali3d.math.Matrix4
import org.rajawali3d.math.vector.Vector3
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer242LightingMapsSpecular(
       val context: Context,
       viewModel: ViewModel
) : RendererBaseClass(context, viewModel), GLSurfaceView.Renderer {

    // VertexBufferObject Ids
    private var vbo = IntArray(1)
    private var cubevao = IntArray(1)
    private var lightvao = IntArray(1)
    private var vboBody = IntArray(1)

    private val lightingShader = Shader()
    private val lampShader = Shader()
    private var diffuseMap = 0
    private var specularMap = 0

    private val camera = Camera(Vector3(0.0, 0.0, 3.0))

    private val lightPos = floatArrayOf(1.2f, 1.0f, 2.0f)

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Use culling to remove back faces.
        //GLES20.glEnable(GLES20.GL_CULL_FACE)

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

      /*  // temporary code
        // Position the eye in front of the origin.
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = -0.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
        // end temporary code*/

        checkGLerr("R01")

        lightingShader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "4.2.lighting_maps.vs",
                "4.2.lighting_maps.fs")
        lampShader.shaderReadCompileLink(
                context,
                Shader.ShaderSource.FROM_ASSETS,
                "4.2.lamp.vs",
                "4.2.lamp.fs")

        checkGLerr("R02")

        glGenVertexArrays(1, cubevao, 0)

        val nativeFloatBuffer = ByteBuffer
                .allocateDirect(vertices.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        nativeFloatBuffer!!.put(vertices).position(0)
        glGenBuffers(1, vbo, 0)
        glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
        glBufferData(GL_ARRAY_BUFFER, vertices.size * 4,
                nativeFloatBuffer, GLES20.GL_STATIC_DRAW)

        checkGLerr("R03")

        glBindVertexArray(cubevao[0])
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4)
        glEnableVertexAttribArray(2)

        checkGLerr("R04")

        // second, configure the light's VAO (VBO stays the same;
        // the vertices are the same for the light object which is also a 3D cube)

        lightvao = IntArray(1)
        glGenVertexArrays(1, lightvao, 0)
        glBindVertexArray(lightvao[0])

        // note that we update the lamp's position attribute's stride to reflect the updated buffer data
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0)
        glEnableVertexAttribArray(0)

        checkGLerr("R05")

        // load textures
        // -----------------------------------------------------------------------------
        diffuseMap = loadTextureFromAsset(context,"container2.png")
        specularMap = loadTextureFromAsset(context,"container2_specular.png")

        // shader configuration
        // --------------------
        lightingShader.use()
        lightingShader.setInt("material.diffuse", 0)
        lightingShader.setInt("material.specular", 1)

        checkGLerr("R06")
    }



    override fun onDrawFrame(glUnused: GL10) {
        Timber.i("OnDrawFrame")

        camera.zoomHack(-1.0)


        glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // be sure to activate shader when setting uniforms/drawing objects
        lightingShader.use()
        lightingShader.setVec3("light.position", lightPos)
        lightingShader.setVec3("viewPos", toFloatArray3(camera.position))

        // light properties
        lightingShader.setVec3("light.ambient", floatArrayOf( 0.2f, 0.2f, 0.2f))
        lightingShader.setVec3("light.diffuse", floatArrayOf( 0.5f, 0.5f, 0.5f))
        lightingShader.setVec3("light.specular", floatArrayOf( 1.0f, 1.0f, 1.0f))

        // material properties
        lightingShader.setFloat("material.shininess", 64.0f)

        // view/projection transformations
        var projection = Matrix4()
        projection = projection.setToPerspective(
                0.1,
                100.0,
                camera.zoom,
                screenWidth * 1.0 / screenHeight * 1.0)
        val view = camera.getViewMatrix()
        lightingShader.setMat4("projection", toFloatArray16(projection))
        lightingShader.setMat4("view", toFloatArray16(view))

        var model = Matrix4() // identity matrix
        lightingShader.setMat4("model", toFloatArray16(model))

        // bind diffuse map
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, diffuseMap)
        // bind specular map
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, specularMap)

        // render the cube
        glBindVertexArray(cubevao[0])
        glDrawArrays(GL_TRIANGLES, 0, 36)

        // also draw the lamp object
        lampShader.use()

        lampShader.setMat4("projection", toFloatArray16(projection))
        lampShader.setMat4("view", toFloatArray16(view))
        model = Matrix4() // identity matrix
        model = model.translate(toVec3(lightPos))
        model = model.scale(Vector3(0.2)) // a smaller cube
        lampShader.setMat4("model", toFloatArray16(model))

        glBindVertexArray(lightvao[0])
        glDrawArrays(GL_TRIANGLES, 0, 36)

        checkGLerr("ODF01")

    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height

        lastX = screenWidth / 2.0f
        lastY = screenHeight / 2.0f

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio * scaleCurrentF
        val right = ratio * scaleCurrentF
        val bottom = -1.0f * scaleCurrentF
        val top = 1.0f * scaleCurrentF
        val near = 1.0f
        val far = 20.0f
    }

    companion object {
        val vertices = floatArrayOf(
                // positions          // normals           // texture coords
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f)
    }

    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)

    private var screenWidth = 0
    private var screenHeight = 0

    /*
    * Store the accumulated touch based manipulation
    */
    private val accumulatedRotation = FloatArray(16)
    private val accumulatedTranslation = FloatArray(16)
    private val accumulatedScaling = FloatArray(16)

    /*
     * Store the current rotation.
     */
    private val incrementalRotation = FloatArray(16)

    private var lastX = 0f
    private var lastY = 0f

}