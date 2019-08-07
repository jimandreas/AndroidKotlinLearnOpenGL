/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidkotlin.opengl.ui

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.androidkotlin.opengl.realtime.Renderer242LightingMapsSpecular
import com.androidkotlin.opengl.realtime.SurfaceViewInstance
import com.androidkotlin.opengl.ui.databinding.FragmentGettingStartedBinding

class OverviewFragment : Fragment() {

    private val viewModel: ViewModel by lazy {
        ViewModelProviders.of(this).get(ViewModel::class.java)
    }
    private lateinit var glSurfaceView: SurfaceViewInstance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentGettingStartedBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.setLocalLifecycleOwner(this)
        binding.viewModel1 = viewModel
        glSurfaceView = binding.myGLSurfaceView
        val renderer = Renderer242LightingMapsSpecular(this.context!!, viewModel)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(renderer)

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        glSurfaceView.setRendererInInstance(renderer, displayMetrics.density, viewModel)

        // Render the view only when there is a change in the drawing data
        // doesn't seem to have much effect now on CPU
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }
}

