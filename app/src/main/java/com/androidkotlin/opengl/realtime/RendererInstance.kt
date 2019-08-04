@file:Suppress(
        /*"unused",
        "unused_variable",*/
        "unused_parameter",
        "unused_property",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "PropertyName")

package com.androidkotlin.opengl.realtime

// translation of
//   LearnOpenGL-master\src\2.lighting\4.2.lighting_maps_specular_map

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.androidkotlin.opengl.ui.GLES20ViewModel
import com.androidkotlin.opengl.util.GraphicsUtils
import com.androidkotlin.opengl.util.Shader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class RendererInstance(
        private val context: Context,
        private val viewModel: GLES20ViewModel
) : GLSurfaceView.Renderer {

    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)
    private val graphicUtils = GraphicsUtils()
    var touchCoordX = 300f
    var touchCoordY = 300f
    val scaleF = 0.1f
    private val INITIAL_SCALE = 0.5f
    @Volatile
    var scaleCurrentF = INITIAL_SCALE


    // update to add touch control - these are set by the SurfaceView class
    // These still work without volatile, but refreshes are not guaranteed to happen.
    @Volatile
    var deltaX: Float = 0.toFloat()
    @Volatile
    var deltaY: Float = 0.toFloat()
    @Volatile
    var deltaTranslateX: Float = 0.toFloat()
    @Volatile
    var deltaTranslateY: Float = 0.toFloat()
    // public volatile float scaleCurrentF = 1.0f;
    // use scale to zoom in initially
//    @Volatile
//    var scaleCurrentF = INITIAL_SCALE
    @Volatile
    var scalePrevious = 0f

    var screenWidth = 0
    var screenHeight = 0


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

    val lightingShader = Shader()
    val lampShader = Shader()

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

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

        lightingShader.shaderReadCompileLink(
                context,
                "4.2.lighting_maps.vs",
                "4.2.lighting_maps.fs")
        lampShader.shaderReadCompileLink(
                context,
                "4.2.lamp.vs",
                "4.2.lamp.fs")


    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height

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

    override fun onDrawFrame(glUnused: GL10) {
        // Timber.i("OnDrawFrame")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if (scaleCurrentF != scalePrevious) {
            onSurfaceChanged(null, screenWidth, screenHeight)  // adjusts view
            scalePrevious = scaleCurrentF
        }


    }


}