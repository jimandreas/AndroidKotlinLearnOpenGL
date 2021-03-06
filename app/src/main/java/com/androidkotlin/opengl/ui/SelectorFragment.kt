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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.androidkotlin.opengl.ui.databinding.FragmentSelectorBinding
import timber.log.Timber

class SelectorFragment : Fragment() {

    private val viewModel: ViewModel by lazy {
        ViewModelProviders.of(this).get(ViewModel::class.java)
    }
    //private lateinit var glSurfaceView: SurfaceViewInstance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentSelectorBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.setLocalLifecycleOwner(this)
        binding.viewModel1 = viewModel

        viewModel.buttonClicked.observe(viewModel.owner, Observer {
            if (it.isNotEmpty()) {
                viewModel.clickProcessed()
                Timber.i("Button click: String: %s", it)

                if (it.isNotEmpty()) {
                    findNavController().navigate(SelectorFragmentDirections.actionShowExercise(it))
                }
            }

        })

        val act = activity as AppCompatActivity
        val sab = act.supportActionBar
        if (sab != null) {
            sab.title = "LOGL"
            sab.setDisplayHomeAsUpEnabled(false)
            sab.setDisplayShowHomeEnabled(false)
        }
        return binding.root
    }
}


// from the earlier hardwired exercise method
//val renderer = GoogleSampleRenderer(this.context!!, viewModel)
//val renderer = Renderer121HelloTriangle(this.context!!, viewModel)
//val renderer = Renderer163CoordinateSystems(this.context!!, viewModel)
//val renderer = Renderer174Camera(this.context!!, viewModel)
//val renderer = Renderer242LightingMapsSpecular(this.context!!, viewModel)
//val renderer = Renderer242LightingMapsPlusCubeObject(this.context!!, viewModel)
//val renderer = Renderer480AdvancedGlslUBO(this.context!!, viewModel)
//val renderer = Renderer4101InstancingQuads(this.context!!, viewModel)
//val renderer = Renderer4102InstancingHacking(this.context!!, viewModel)
//val renderer = Renderer4103AdvancedAsteroidsInstanced(this.context!!, viewModel)


//        glSurfaceView.setEGLContextClientVersion(3)
//        glSurfaceView.setRenderer(renderer)
//
//        val displayMetrics = DisplayMetrics()
//        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        glSurfaceView.setRendererInInstance(renderer, displayMetrics.density, viewModel)
//
//        // Render the view only when there is a change in the drawing data
//        // doesn't seem to have much effect now on CPU
//        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

