@file:Suppress(
        "unused",
        "unused_variable",
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
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import com.androidkotlin.opengl.math.Matrix4
import com.androidkotlin.opengl.ui.ViewModel
import com.androidkotlin.opengl.util.*
import com.androidkotlin.opengl.math.Vector3
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open class RendererBaseClass(
        context: Context,
        viewModel: ViewModel
) {

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

}