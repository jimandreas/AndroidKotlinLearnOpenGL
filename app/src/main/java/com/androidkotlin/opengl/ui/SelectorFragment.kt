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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androidkotlin.opengl.databinding.FragmentSelectorBinding
import timber.log.Timber

class SelectorFragment : Fragment() {

    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // the *Binding is generated - it will not be valid until after a build
        val binding = FragmentSelectorBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.setLocalLifecycleOwner(this)
        binding.viewModel1 = viewModel

        viewModel.buttonClicked.observe(viewModel.owner, Observer {
            if (it.isNotEmpty()) {
                viewModel.clickProcessed()
                Timber.i("Button click: String: %s", it)

                // the *Directions is generated, see:
                //  build/generated/source/navigation-args/debug/com/androidkotlin/opengl/ui/SelectorFragmentDirections.java
                findNavController().navigate(SelectorFragmentDirections.actionShowExercise(it))
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