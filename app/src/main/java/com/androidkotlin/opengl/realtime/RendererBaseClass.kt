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
import com.androidkotlin.opengl.ui.ViewModel


open class RendererBaseClass(
        context: Context,
        viewModel: ViewModel
) {

    var touchCoordX = 300f
    var touchCoordY = 300f
    val scaleF = 0.1f

    @Volatile
    var scaleCurrentF = INITIAL_SCALE
    @Volatile
    var scalePrevious = 0f
    @Volatile
    var deltaX: Float = 0f
    @Volatile
    var deltaY: Float = 0f
    @Volatile
    var deltaTranslateX: Float = 0f
    @Volatile
    var deltaTranslateY: Float = 0f

    @Volatile
    var deltaZoom: Float = 0f

    companion object {
        private const val INITIAL_SCALE = 0.5f
    }

}