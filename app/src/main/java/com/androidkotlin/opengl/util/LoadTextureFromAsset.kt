/*
*
// The MIT License (MIT)
//
// Copyright (c) 2013 Dan Ginsburg, Budirijanto Purnomo
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

//
// Book:      OpenGL(R) ES 3.0 Programming Guide, 2nd Edition
// Authors:   Dan Ginsburg, Budirijanto Purnomo, Dave Shreiner, Aaftab Munshi
// ISBN-10:   0-321-93388-5
// ISBN-13:   978-0-321-93388-1
// Publisher: Addison-Wesley Professional
// URLs:      http://www.opengles-book.com
//            http://my.safaribooksonline.com/book/animation-and-3d/9780133440133
//

Original source:

https://raw.githubusercontent.com/danginsburg/opengles3-book/master/Android_Java/Chapter_10/MultiTexture/src/com/openglesbook/multitexture/MultiTextureRenderer.java

* */

package com.androidkotlin.opengl.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glGenerateMipmap
import android.opengl.GLES30
import android.opengl.GLUtils
import timber.log.Timber

import java.io.IOException
import java.io.InputStream


// TODO: check out "Methods for encoding and decoding ETC1 textures. "

//
//  Load texture from asset
//
fun loadTextureFromAsset(context: Context, fileName: String): Int {
    val textureId = IntArray(1)
    val inputStream = context.assets.open(fileName)

    val bitmap = BitmapFactory.decodeStream(inputStream)

    checkGLerr("lTFA01")

    GLES30.glGenTextures(1, textureId, 0)
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId[0])

    GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)

    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)

    checkGLerr("lTFA02")

    return textureId[0]
}

//
//  Load texture from asset
//
fun loadTextureFromAsset163(context: Context, fileName: String): Int {
    val textureId = IntArray(1)
    val inputStream = context.assets.open(fileName)

    val bitmap = BitmapFactory.decodeStream(inputStream)

    checkGLerr("lTFA01")

    GLES30.glGenTextures(1, textureId, 0)
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId[0])
    // wrapping parms
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)
    // texture filtering parms
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
    // load image, create texture and generate mipmaps
    GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
    glGenerateMipmap(GL_TEXTURE_2D)
    checkGLerr("lTFA02")

    inputStream.close()
    Timber.i("texture bitmap file: %s w: %d h: %d", fileName, bitmap.width, bitmap.height)
    return textureId[0]
}
