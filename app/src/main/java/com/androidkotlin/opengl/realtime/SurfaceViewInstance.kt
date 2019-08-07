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

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Scroller
import com.androidkotlin.opengl.ui.ViewModel
import timber.log.Timber
import kotlin.math.atan2
import kotlin.math.sqrt

/*
 * OpenGL rendering space
 */

@Suppress("ConstantConditionIf")
class SurfaceViewInstance : GLSurfaceView {
    var mSelectMode = false
    private var mLastTouchState = NO_FINGER_DOWN

    private lateinit var renderer: Renderer242LightingMapsSpecular

    private lateinit var scroller: Scroller
    private lateinit var gestureDetector: GestureDetector
    private lateinit var viewModel: ViewModel
    private lateinit var contextLocal: Context

    // Offsets for touch events
    private var mPreviousX: Float = 0.toFloat()
    private var mPreviousY: Float = 0.toFloat()
    private var density: Float = 0.toFloat()
    private var mInitialSpacing: Float = 0.toFloat()

    private var mOldX = 0f
    private var mOldY = 0f

    private val isAnimationRunning: Boolean
        get() = !scroller.isFinished

    constructor(contextIn: Context) : super(contextIn) {
        init(contextIn)
    }

    constructor(contextIn: Context, attrs: AttributeSet) : super(contextIn, attrs) {
        init(contextIn)
    }

    private fun init(contextIn: Context) {

        contextLocal = contextIn
        scroller = Scroller(contextLocal, null, true)

        // The scroller doesn't have any built-in animation functions--it just supplies
        // values when we ask it to. So we have to have a way to call it every frame
        // until the fling ends. This code (ab)uses a ValueAnimator object to generate
        // a callback on every animation frame. We don't use the animated value at all.

        val mScrollAnimator = ValueAnimator.ofFloat(0f, 1f)
        mScrollAnimator.addUpdateListener {
            // tickScrollAnimation();
        }
        // Create a gesture detector to handle onTouch messages (experimental)
        gestureDetector = GestureDetector(contextLocal, GestureListener())

        // Turn off long press--this control doesn't use it, and if long press is enabled,
        // you can't scroll for a bit, pause, then scroll some more (the pause is interpreted
        // as a long press, apparently)
        gestureDetector.setIsLongpressEnabled(false)
    }

    fun setRendererInInstance(
            rendererIn: Renderer242LightingMapsSpecular,
            densityIn: Float,
            viewModelIn: ViewModel) {
        renderer = rendererIn
        density = densityIn
        viewModel = viewModelIn


//        viewModel.wireFrameModeOn.observe(viewModel.owner, Observer {
//            renderer.wireFrameRenderingFlag = it
//            requestRender()
//        })
//
//        viewModel.shaderToggle.observe(viewModel.owner, Observer {
//            renderer.shaderProgramToggle = it
//            requestRender()
//        })
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

        // hand the event to the GestureDetector
        // ignore the result for now.
        // TODO:  hook up fling logic
        val result = gestureDetector.onTouchEvent(m)

        if (m == null) {
            Timber.i("OnTouch: null m!")
            return true
        }
        if (hack) renderMode = RENDERMODE_CONTINUOUSLY

        if (m.action == MotionEvent.ACTION_UP) {
            renderMode = RENDERMODE_WHEN_DIRTY
            //Timber.i("OnTouch: release")
            return true
        }

        //Number of touches
        val pointerCount = m.pointerCount
        //Timber.i("OnTouch: pointer count %d", pointerCount)
        when {
            pointerCount > 2 -> {
                mLastTouchState = MORE_FINGERS
                return true
            }
            pointerCount == 2 -> {
                if (mSelectMode) return true
                val action = m.actionMasked
                val actionIndex = m.actionIndex
                if (mLastTouchState == MORE_FINGERS) {
                    x1 = m.getX(0)
                    y1 = m.getY(0)
                    x2 = m.getX(1)
                    y2 = m.getY(1)

                    renderer.touchCoordX = m.x
                    renderer.touchCoordY = m.y

                    mOldX = (x1 + x2) / 2.0f
                    mOldY = (y1 + y2) / 2.0f
                    mLastTouchState = TWO_FINGERS_DOWN
                    return true
                }
                when (action) {
                    MotionEvent.ACTION_MOVE -> {

                        x1 = m.getX(0)
                        y1 = m.getY(0)
                        x2 = m.getX(1)
                        y2 = m.getY(1)

                        renderer.touchCoordX = m.x
                        renderer.touchCoordY = m.y

                        deltax = (x1 + x2) / 2.0f
                        deltax -= mOldX
                        deltay = (y1 + y2) / 2.0f
                        deltay -= mOldY

                        renderer.deltaTranslateX = renderer.deltaTranslateX + deltax / (density * 300f)
                        renderer.deltaTranslateY = renderer.deltaTranslateY - deltay / (density * 300f)

                        mOldX = (x1 + x2) / 2.0f
                        mOldY = (y1 + y2) / 2.0f

                        val mCurrentSpacing = spacing(m)

                        if (mLastTouchState != TWO_FINGERS_DOWN) {
                            mInitialSpacing = spacing(m)
                        } else {
                            deltaSpacing = mCurrentSpacing - mInitialSpacing
                            deltaSpacing /= mInitialSpacing


                            // TODO: adjust this exponent.
                            //   for now, hack into buckets
                            if (renderer.scaleCurrentF < 0.1f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 1000f
                            } else if (renderer.scaleCurrentF < 0.1f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 500f
                            } else if (renderer.scaleCurrentF < 0.5f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 200f
                            } else if (renderer.scaleCurrentF < 1f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 50f
                            } else if (renderer.scaleCurrentF < 2f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 10f
                            } else if (renderer.scaleCurrentF < 5f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 10f
                            } else if (renderer.scaleCurrentF > 5f) {
                                if (deltaSpacing > 0) {
                                    renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 10f
                                }
                            }
                            //                        Log.w("Move", "Spacing is " + renderer.scaleCurrentF + " spacing = " + deltaSpacing);


                        }
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        // Log.w("touch POINTER DOWN", "");

                        x1 = m.getX(0)
                        y1 = m.getY(0)
                        x2 = m.getX(1)
                        y2 = m.getY(1)

                        renderer.touchCoordX = m.x
                        renderer.touchCoordY = m.y

                        mOldX = (x1 + x2) / 2.0f
                        mOldY = (y1 + y2) / 2.0f
                        mInitialSpacing = spacing(m)
                    }
                    MotionEvent.ACTION_POINTER_UP -> if (hack) renderMode = RENDERMODE_WHEN_DIRTY
                }// Log.w("Down", "touch DOWN, mInitialSpacing is " + mInitialSpacing);
                mLastTouchState = TWO_FINGERS_DOWN
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
                    if (mLastTouchState != ONE_FINGER_DOWN) {  // handle anything to one finger interaction
                        mLastTouchState = ONE_FINGER_DOWN
                    } else {
                        val deltaX = (x - mPreviousX) / density / 2f
                        val deltaY = (y - mPreviousY) / density / 2f

                        renderer.deltaX = renderer.deltaX + deltaX
                        renderer.deltaY = renderer.deltaY + deltaY
                        // Log.w("touch", ": mDX = " + renderer.deltaX + " mDY = " + renderer.deltaY);
                    }
                }
                mPreviousX = x
                mPreviousY = y

                return true
            }
            else -> {
                if (hack) {
                    renderMode = RENDERMODE_WHEN_DIRTY
                }
                //Timber.i("OnTouch: Release")
                return true
            }


        }
        //return super.onTouchEvent(m)
    }

    /**
     * Determine the space between the first two fingers
     */
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private fun midPoint(point: PointF, event: MotionEvent) {
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


    /**
     * Extends [GestureDetector.SimpleOnGestureListener] to provide custom gesture
     * processing.
     */
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {

            // Timber.w("onScroll");
            return true
        }

        // not implemented - probably a bad idea
        //   might be good to average out the pivot to help with jitter
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {

            // Timber.w("onFling");
            //            // Set up the Scroller for a fling
            //            float scrollTheta = vectorToScalarScroll(
            //                    velocityX,
            //                    velocityY,
            //                    e2.getX() - mPieBounds.centerX(),
            //                    e2.getY() - mPieBounds.centerY());
            //            scroller.fling(
            //                    0,
            //                    (int) getPieRotation(),
            //                    0,
            //                    (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
            //                    0,
            //                    0,
            //                    Integer.MIN_VALUE,
            //                    Integer.MAX_VALUE);
            //
            //            // Start the animator and tell it to animate for the expected duration of the fling.
            //            if (Build.VERSION.SDK_INT >= 11) {
            //                mScrollAnimator.setDuration(scroller.getDuration());
            //                mScrollAnimator.start();
            //            }
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {

            if (isAnimationRunning) {
                stopScrolling()
            }
            return true
        }
    }

    /**
     * Force a stop to all pie motion. Called when the user taps during a fling.
     */
    private fun stopScrolling() {
        scroller.forceFinished(true)

        onScrollFinished()
    }

    /**
     * Called when the user finishes a scroll action.
     */
    private fun onScrollFinished() {

    }


    companion object {

        private const val NO_FINGER_DOWN = 0
        private const val ONE_FINGER_DOWN = 1
        private const val TWO_FINGERS_DOWN = 2
        private const val MORE_FINGERS = 3

        private const val hack = true   // play with Rendermode
    }
}