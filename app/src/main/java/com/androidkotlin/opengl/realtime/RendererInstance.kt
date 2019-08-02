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

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.androidkotlin.opengl.ui.GLES20ViewModel
import com.androidkotlin.opengl.util.GraphicsUtils
import com.androidkotlin.opengl.util.Vector3
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/*
https://stackoverflow.com/questions/4117865/what-is-the-difference-between-android-opengl-and-javax-microedition-khronos-ope
Khronos vs android.opengl
 */
class RendererInstance(
        val viewModel : GLES20ViewModel
        ) : GLSurfaceView.Renderer {

//    val scaleF = 1.5f
    val scaleF = 0.1f
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)
    private val graphicUtils = GraphicsUtils()
    var touchCoordX = 300f
    var touchCoordY = 300f

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
    @Volatile
    var scaleCurrentF = INITIAL_SCALE
    @Volatile
    var scalePrevious = 0f

    /*
     * Store the model matrix. This matrix is used to move models from object space
     * (where each model can be thought of being located at the center of the universe)
     * to world space.
     */
    private val modelMatrix = FloatArray(16)

    /*
     * Store the view matrix. This can be thought of as our camera.
     * This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private val viewMatrix = FloatArray(16)

    /*
     * Store the projection matrix.
     * This is used to project the scene onto a 2D viewport.
     */
    private val projectionMatrix = FloatArray(16)

    /*
     * Allocate storage for the final combined matrix.
     * This will be passed into the shader program.
     */
    private val mvpMatrix = FloatArray(16)

    /*
     * Stores a copy of the model matrix specifically for the light position.
     */
    private val lightModelMatrix = FloatArray(16)

    /*
     * This will be used to pass in the transformation matrix.
     */
    private var mvpMatrixHandle: Int = 0

    /*
     * This will be used to pass in the modelview matrix.
     */
    private var mvMatrixHandle: Int = 0

    /*
     * This will be used to pass in the light position.
     */
    private var lightPosHandle: Int = 0

    /*
     * These will be used to pass in model
     * position, color and normal information.
     */
    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var normalHandle: Int = 0

    /*
     * Used to hold a light centered on the origin in model space.
     * We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.
     */
    private val lightPosInModelSpace = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

    /*
     * Used to hold the current position of the light
     * in world space (after transformation via model matrix),
     * and then in eye space (after transformation via modelview matrix)
     */
    private val lightPosInWorldSpace = FloatArray(4)
    private val lightPosInEyeSpace = FloatArray(4)

    var shaderProgramToggle = false
    /*
     * This is a handle to our per-vertex cube shading program.
     */
    private var perVertexProgramHandle = -1
    /*
     * This is a handle to our per-pixel cube shading program.
     */
    private var perPixelProgramHandle: Int = 0

    var wireFrameRenderingFlag = true
    private var mSelectModeFlag = false
    /*
     * This is a handle to our light point program.
     */
    private var pointProgramHandle: Int = 0
    /*
     * A temporary matrix.
     */
    private val temporaryMatrix = FloatArray(16)

    /*
     * Store the accumulated rotation.
     */
    private val accumulatedRotation = FloatArray(16)
    private val accumulatedTranslation = FloatArray(16)
    private val accumulatedScaling = FloatArray(16)

    /*
     * Store the current rotation.
     */
    private val incrementalRotation = FloatArray(16)

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

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        var vertexShader = graphicUtils.vertexShaderLesson2
        var fragmentShader = graphicUtils.fragmentShaderLesson2
        var vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        var fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)

        perVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                arrayOf("a_Position", "a_Color", "a_Normal"))

        /* add in a pixel shader from lesson 3 - switchable */
        vertexShader = graphicUtils.vertexShaderLesson3
        fragmentShader = graphicUtils.fragmentShaderLesson3
        vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)
        perPixelProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                arrayOf("a_Position", "a_Color", "a_Normal"))

        // Define a simple shader program for our point (the orbiting light source)
        val pointVertexShader = ("uniform mat4 u_MVPMatrix;      \n"
                + "attribute vec4 a_Position;     \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_Position = u_MVPMatrix   \n"
                + "               * a_Position;   \n"
                + "   gl_PointSize = 5.0;         \n"
                + "}                              \n")

        val pointFragmentShader = ("precision mediump float;       \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_FragColor = vec4(1.0,    \n"
                + "   1.0, 1.0, 1.0);             \n"
                + "}                              \n")

        val pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader)
        val pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader)
        pointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                arrayOf("a_Position"))

        // Initialize the modifier matrices
        Matrix.setIdentityM(accumulatedRotation, 0)
        Matrix.setIdentityM(accumulatedTranslation, 0)
        Matrix.setIdentityM(accumulatedScaling, 0)

    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)
        mWidth = width
        mHeight = height

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio * scaleCurrentF
        val right = ratio * scaleCurrentF
        val bottom = -1.0f * scaleCurrentF
        val top = 1.0f * scaleCurrentF
        val near = 1.0f
        val far = 20.0f
        // final float far = 5.0f;  nothing visible

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)

        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("GLERROR: $glError")
        }
    }

    override fun onDrawFrame(glUnused: GL10) {
        // Timber.i("OnDrawFrame")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if (scaleCurrentF != scalePrevious) {
            onSurfaceChanged(null, mWidth, mHeight)  // adjusts view
            scalePrevious = scaleCurrentF
        }

        // move the view as necessary if the user has shifted it manually
        Matrix.translateM(viewMatrix, 0, deltaTranslateX, deltaTranslateY, 0.0f)
        deltaTranslateX = 0.0f
        deltaTranslateY = 0.0f

        // Set our per-vertex lighting program.
        val mSelectedProgramHandle = if (shaderProgramToggle) {
            perVertexProgramHandle
        } else {
            perPixelProgramHandle
        }

        GLES20.glUseProgram(mSelectedProgramHandle)
        // Set program handles for drawing.
        mvpMatrixHandle = GLES20.glGetUniformLocation(mSelectedProgramHandle, "u_MVPMatrix")
        mvMatrixHandle = GLES20.glGetUniformLocation(mSelectedProgramHandle, "u_MVMatrix")
        lightPosHandle = GLES20.glGetUniformLocation(mSelectedProgramHandle, "u_LightPos")
        positionHandle = GLES20.glGetAttribLocation(mSelectedProgramHandle, "a_Position")
        colorHandle = GLES20.glGetAttribLocation(mSelectedProgramHandle, "a_Color")
        normalHandle = GLES20.glGetAttribLocation(mSelectedProgramHandle, "a_Normal")

        // Calculate position of the light. Push into the distance.
        Matrix.setIdentityM(lightModelMatrix, 0)
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, -1.0f)

        Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModelMatrix, 0, lightPosInModelSpace, 0)
        Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInWorldSpace, 0)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -2.5f)
        Matrix.scaleM(modelMatrix, 0, scaleF, scaleF, scaleF)
        doMatrixSetup()
        // modelManager.renderModel(positionHandle, colorHandle, normalHandle)
        // DEBUG:  box in scene center
        // mBoundingBox.render(positionHandle, colorHandle, normalHandle, wireFrameRenderingFlag);
    }


    private fun doMatrixSetup() {
        /*
         * Set a matrix that contains the additional *incremental* rotation
         * as indicated by the user touching the screen
         */

        if (!mSelectModeFlag) {
            Matrix.setIdentityM(incrementalRotation, 0)
            Matrix.rotateM(incrementalRotation, 0, deltaX, 0.0f, 1.0f, 0.0f)
            Matrix.rotateM(incrementalRotation, 0, deltaY, 1.0f, 0.0f, 0.0f)
            deltaX = 0.0f
            deltaY = 0.0f

            // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
            Matrix.multiplyMM(temporaryMatrix, 0, incrementalRotation, 0, accumulatedRotation, 0)
            System.arraycopy(temporaryMatrix, 0, accumulatedRotation, 0, 16)
        }
        // Rotate the object taking the overall rotation into account.
        Matrix.multiplyMM(temporaryMatrix, 0, modelMatrix, 0, accumulatedRotation, 0)
        System.arraycopy(temporaryMatrix, 0, modelMatrix, 0, 16)

        // This multiplies the view matrix by the model matrix, and stores
        // the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, mvpMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(temporaryMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)
        System.arraycopy(temporaryMatrix, 0, mvpMatrix, 0, 16)

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        // Pass in the light position in eye space.
        GLES20.glUniform3f(lightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2])

        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
    }

    /*
     * this is a special setup -
     *    that feeds a uniform matrix to the shader as the Model + View matrix.
     *    This is then combined with the Projection matrix for the MVP matrix.
     *    The result is a matrix that doesn't change with the model and view -
     *    used for pointer rendering and opengl.
     */
    private fun doMatrixSetupViewOnly() {

        // Pass in the modelview matrix.
        // GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, mvpMatrix, 0);
        Matrix.setIdentityM(temporaryMatrix, 0)
        GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, temporaryMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(temporaryMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temporaryMatrix, 0, mvpMatrix, 0, 16)

        // Pass in the combined matrix.
        // GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, projectionMatrix, 0)

        // Pass in the light position in eye space.
        GLES20.glUniform3f(lightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2])

        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
    }

    /*
     * Draws a point representing the position of the light.
     */
    private fun drawLight() {
        val pointMVPMatrixHandle = GLES20.glGetUniformLocation(pointProgramHandle, "u_MVPMatrix")
        val pointPositionHandle = GLES20.glGetAttribLocation(pointProgramHandle, "a_Position")

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, lightPosInModelSpace[0], lightPosInModelSpace[1], lightPosInModelSpace[2])

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle)

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, lightModelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }

    /*
     * Helper function to compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    private fun compileShader(shaderType: Int, shaderSource: String): Int {
        var shaderHandle: Int = 0
        try {
            shaderHandle = GLES20.glCreateShader(shaderType)

            if (shaderHandle != 0) {
                // Pass in the shader source.
                GLES20.glShaderSource(shaderHandle, shaderSource)

                // Compile the shader.
                GLES20.glCompileShader(shaderHandle)

                // Get the compilation status.
                val compileStatus = IntArray(1)
                GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

                // If the compilation failed, delete the shader.
                if (compileStatus[0] == 0) {
                    Timber.e("Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle))
                    GLES20.glDeleteShader(shaderHandle)
                    shaderHandle = 0
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception in compile Shader********************")
        }

        if (shaderHandle == 0) {
            throw RuntimeException("Error creating shader.")
        }

        return shaderHandle
    }

    /*
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes           Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    private fun createAndLinkProgram(vertexShaderHandle: Int, fragmentShaderHandle: Int, attributes: Array<String>?): Int {
        var programHandle = GLES20.glCreateProgram()

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle)

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle)

            // Bind attributes
            if (attributes != null) {
                val size = attributes.size
                for (i in 0 until size) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i])
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle)

            // Get the link status.
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Timber.e("Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle))
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }

        if (programHandle == 0) {
            throw RuntimeException("Error creating program.")
        }

        return programHandle
    }


    companion object {
        private val mScreenVector1 = Vector3()
        private val mScreenVector2 = Vector3()
        private val mTempVector1 = Vector3()
        private val mTempVector2 = Vector3()
        private const val INITIAL_SCALE = 0.5f
        private var mSaveScale = 0f
        private var mHeight: Int = 0
        private var mWidth: Int = 0
        private val mViewport = intArrayOf(0, 0, 0, 0)
    }
}