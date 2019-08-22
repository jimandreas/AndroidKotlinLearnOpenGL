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

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Scroller
import com.androidkotlin.opengl.ui.ViewModel
import timber.log.Timber

/*
 * OpenGL rendering space
 */

@Suppress("ConstantConditionIf")
class SurfaceViewInstance : GLSurfaceView {
    private var lastTouchState = NO_FINGER_DOWN

    //private lateinit var renderer: Renderer242LightingMapsSpecular
    private lateinit var renderer: RendererBaseClass
    private lateinit var viewModel: ViewModel
    private lateinit var contextLocal: Context

    // Offsets for touch events
    private var previousX: Float = 0.toFloat()
    private var previousY: Float = 0.toFloat()
    private var density: Float = 0.toFloat()
    private var initialSpacing: Float = 0.toFloat()

    private var oldX = 0f
    private var oldY = 0f

    constructor(contextIn: Context) : super(contextIn) {
        init(contextIn)
    }

    constructor(contextIn: Context, attrs: AttributeSet) : super(contextIn, attrs) {
        init(contextIn)
    }

    private fun init(contextIn: Context) {

        contextLocal = contextIn
    }

    fun setRendererInInstance(
            rendererIn: RendererBaseClass,
            densityIn: Float,
            viewModelIn: ViewModel) {
        renderer = rendererIn
        density = densityIn
        viewModel = viewModelIn
    }

    // with h/t to :
    // http://stackoverflow.com/questions/14818530/how-to-implement-a-two-finger-drag-gesture-on-android
    // and:
    // http://judepereira.com/blog/multi-touch-in-android-translate-scale-and-rotate/

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(m: MotionEvent?): Boolean {
        val x1: Float
        val x2: Float
        val y1: Float
        val y2: Float
        var deltax: Float
        var deltay: Float
        var deltaSpacing: Float

        if (m == null) {
            Timber.i("OnTouch: null m!")
            return true
        }
        renderMode = RENDERMODE_CONTINUOUSLY

        if (m.action == MotionEvent.ACTION_UP) {
            renderMode = RENDERMODE_WHEN_DIRTY
            //Timber.i("OnTouch: release")
            return true
        }

        //Number of touches
        val pointerCount = m.pointerCount
        Timber.i("OnTouch: pointer count %d", pointerCount)
        when {
            pointerCount > 1 -> {
                lastTouchState = MORE_FINGERS
                return true
            }
            pointerCount == 1 -> {
                /*
                 * handle single finger swipe - rotate each item
                 */
                val x = m.x
                val y = m.y

                renderer.touchCoordX = m.x
                renderer.touchCoordY = m.y

                if (m.action == MotionEvent.ACTION_MOVE) {
                    if (lastTouchState != ONE_FINGER_DOWN) {  // handle anything to one finger interaction
                        lastTouchState = ONE_FINGER_DOWN
                    } else {
                        val deltaX = -(x - previousX) / density / 100f
                        val deltaY = (y - previousY) / density / 100f

                        renderer.deltaX = renderer.deltaX + deltaX
                        renderer.deltaY = renderer.deltaY + deltaY
                        Timber.i("Rotate: dx: %5.2f dy %5.2f density: %5.2f",
                                deltaX, deltaY, density)
                    }
                }
                previousX = x
                previousY = y

                return true
            }
            else -> {
                renderMode = RENDERMODE_WHEN_DIRTY
                //Timber.i("OnTouch: Release")
                return true
            }
        }
    }

    companion object {
        private const val NO_FINGER_DOWN = 0
        private const val ONE_FINGER_DOWN = 1
        private const val TWO_FINGERS_DOWN = 2
        private const val MORE_FINGERS = 3
    }
}