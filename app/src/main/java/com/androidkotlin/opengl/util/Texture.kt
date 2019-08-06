package com.androidkotlin.opengl.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition


// Note that JDV uses "stbi_load" to pull in the image.
//   -- https://raw.githubusercontent.com/JoeyDeVries/LearnOpenGL/master/src/2.lighting/4.2.lighting_maps_specular_map/lighting_maps_specular.cpp

class Texture {

    fun loadTexture(context: Context, fileName: String) {


        val target = Glide.with(context)
                .load(Uri.parse("file:///android_asset/$fileName"))
                .into(object : Target<Drawable> {
                    override fun onStart() {

                    }

                    override fun onStop() {

                    }

                    override fun onDestroy() {

                    }

                    override fun onLoadStarted(placeholder: Drawable?) {

                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {

                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun getSize(cb: SizeReadyCallback) {

                    }

                    override fun removeCallback(cb: SizeReadyCallback) {

                    }

                    override fun setRequest(request: Request?) {

                    }

                    override fun getRequest(): Request? {
                        return null
                    }
                })
    }
}