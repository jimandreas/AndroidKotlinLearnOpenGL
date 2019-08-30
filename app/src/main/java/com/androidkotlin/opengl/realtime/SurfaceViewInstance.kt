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
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.androidkotlin.opengl.ui.ViewModel
import timber.log.Timber
import kotlin.math.atan2
import kotlin.math.sqrt

/*
 * OpenGL rendering space
 */

@Suppress("ConstantConditionIf")
class SurfaceViewInstance : GLSurfaceView {
    private var lastTouchState = NO_FINGER_DOWN

    //private lateinit var r: Renderer242LightingMapsSpecular
    private lateinit var r: RendererBaseClass
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
        r = rendererIn
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
            pointerCount > 2 -> {
                lastTouchState = MORE_FINGERS
                return true
            }
            pointerCount == 1 -> {
                /*
                 * handle single finger swipe - rotate
                 */
                x1 = m.x
                y1 = m.y

                if (m.action == MotionEvent.ACTION_MOVE) {
                    if (lastTouchState != ONE_FINGER_DOWN) {  // handle anything to one finger interaction
                        lastTouchState = ONE_FINGER_DOWN
                    } else {
                        val deltaX = -(x1 - previousX) / density / 100f
                        val deltaY = (y1 - previousY) / density / 100f
                        r.deltaX = r.deltaX + deltaX
                        r.deltaY = r.deltaY + deltaY
                        Timber.i("Rotate: dx: %5.2f dy %5.2f density: %5.2f",
                                deltaX, deltaY, density)
                    }
                }
                r.touchCoordX = x1
                r.touchCoordY = y1
                previousX = x1
                previousY = y1
                return true
            }
            pointerCount == 2 -> {
                val action = m.actionMasked
                val actionIndex = m.actionIndex
                if (lastTouchState == MORE_FINGERS) {
                    x1 = m.getX(0)
                    y1 = m.getY(0)
                    x2 = m.getX(1)
                    y2 = m.getY(1)

                    r.touchCoordX = x1
                    r.touchCoordY = y1

                    oldX = (x1 + x2) / 2.0f
                    oldY = (y1 + y2) / 2.0f
                    lastTouchState = TWO_FINGERS_DOWN
                    return true
                }
                when (action) {
                    MotionEvent.ACTION_MOVE -> {

                        x1 = m.getX(0)
                        y1 = m.getY(0)
                        x2 = m.getX(1)
                        y2 = m.getY(1)

                        r.touchCoordX = x1
                        r.touchCoordY = y1

                        deltax = (x1 + x2) / 2.0f
                        deltax -= oldX
                        deltay = (y1 + y2) / 2.0f
                        deltay -= oldY

                        r.deltaTranslateX = r.deltaTranslateX + deltax / (density * 300f)
                        r.deltaTranslateY = r.deltaTranslateY - deltay / (density * 300f)

                        oldX = (x1 + x2) / 2.0f
                        oldY = (y1 + y2) / 2.0f

                        val currentSpacing = spacingBetweenFingers(m)

                        if (lastTouchState != TWO_FINGERS_DOWN) {
                            initialSpacing = spacingBetweenFingers(m)
                        } else {
                            deltaSpacing = currentSpacing - initialSpacing
                            deltaSpacing /= initialSpacing

                            if (deltaSpacing < 0.0) {
                                r.deltaZoom += 0.5f
                            } else {
                                r.deltaZoom -= 0.5f
                            }

                            //   this code is left in for scaling objects.
                            //   The LearnOpenGL exercises are better suited to
                            //   a walk-around (FPS) camera operation.
                            // TODO: adjust this exponent.
                            //   for now, hack into buckets
                            if (r.scaleCurrentF < 0.1f) {
                                r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 1000f
                            } else if (r.scaleCurrentF < 0.1f) {
                                r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 500f
                            } else if (r.scaleCurrentF < 0.5f) {
                                r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 200f
                            } else if (r.scaleCurrentF < 1f) {
                                r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 50f
                            } else if (r.scaleCurrentF < 2f) {
                                r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 10f
                            } else if (r.scaleCurrentF < 5f) {
                                r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 10f
                            } else if (r.scaleCurrentF > 5f) {
                                if (deltaSpacing > 0) {
                                    r.scaleCurrentF = r.scaleCurrentF + -deltaSpacing / 10f
                                }
                            }
                        }
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        x1 = m.getX(0)
                        y1 = m.getY(0)
                        x2 = m.getX(1)
                        y2 = m.getY(1)

                        r.touchCoordX = m.x
                        r.touchCoordY = m.y

                        oldX = (x1 + x2) / 2.0f
                        oldY = (y1 + y2) / 2.0f
                        initialSpacing = spacingBetweenFingers(m)
                    }
                    MotionEvent.ACTION_POINTER_UP -> renderMode = RENDERMODE_WHEN_DIRTY
                }
                lastTouchState = TWO_FINGERS_DOWN
                return true
            }
            else -> {
                renderMode = RENDERMODE_WHEN_DIRTY
                //Timber.i("OnTouch: Release")
                return true
            }
        }
    }

    /**
     * Determine the space between the first two fingers
     */
    private fun spacingBetweenFingers(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private fun midPointBetweenFingers(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    /*
     * Calculate the degree to be rotated by.
     */
    private fun rotation(event: MotionEvent): Float {
        val deltaX = (event.getX(0) - event.getX(1)).toDouble()
        val deltaY = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(deltaY, deltaX)
        return Math.toDegrees(radians).toFloat()
    }


    companion object {
        private const val NO_FINGER_DOWN = 0
        private const val ONE_FINGER_DOWN = 1
        private const val TWO_FINGERS_DOWN = 2
        private const val MORE_FINGERS = 3
    }
}