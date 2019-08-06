package com.androidkotlin.opengl.util

import android.opengl.GLES20
import timber.log.Timber

fun checkGLerr(str: String) {
    val glError = GLES20.glGetError()
    if (glError != GLES20.GL_NO_ERROR) {
        Timber.e("*********************************")
        Timber.e("$str: $glError  (0x%X)", glError)
        Timber.e("*********************************")
    }
}