package com.androidkotlin.opengl.ui

import android.graphics.Typeface.*
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.text.*
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.androidkotlin.opengl.googlesample.GoogleSampleRenderer
import com.androidkotlin.opengl.learnopengl.*
import com.androidkotlin.opengl.realtime.RendererBaseClass
import com.androidkotlin.opengl.realtime.SurfaceViewInstance
import com.androidkotlin.opengl.databinding.FragmentExerciseBinding


enum class Affordances { NONE, SPIN, FULL }

class ExerciseFragment : Fragment() {

    private val viewModel: ViewModel by viewModels()
    private lateinit var glSurfaceView: SurfaceViewInstance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentExerciseBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.setLocalLifecycleOwner(this)
        binding.viewModel1 = viewModel
        glSurfaceView = binding.learnOpenglSurfaceView

        val act = activity as AppCompatActivity
        val sab = act.supportActionBar

        val whichExercise = ExerciseFragmentArgs.fromBundle(requireArguments()).selectedExercise

        if (sab != null) {
            sab.title = whichExercise
            sab.setDisplayHomeAsUpEnabled(true)
            sab.setDisplayShowHomeEnabled(true)
        }

        //   Ooops this is the system back button!
        //   I need the home button
        // https://stackoverflow.com/questions/56640729/implementing-custom-back-navigation-in-android
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            Timber.e("WOW back pressed!!")
//        }



        when (whichExercise) {
            "GoogleSample" -> setupRenderer(GoogleSampleRenderer(this.requireContext(), viewModel), Affordances.SPIN)
            "HelloTriangle" -> setupRenderer(Renderer121HelloTriangle(this.requireContext(), viewModel), Affordances.NONE)
            "CoordinateSystems" -> setupRenderer(Renderer163CoordinateSystems(this.requireContext(), viewModel), Affordances.NONE)
            "Camera" -> setupRenderer(Renderer174Camera(this.requireContext(), viewModel), Affordances.FULL)
            "LightingMaps" -> setupRenderer(Renderer242LightingMapsSpecular(this.requireContext(), viewModel), Affordances.FULL)
            "LightingMaps+Cube" -> setupRenderer(Renderer242LightingMapsPlusCubeObject(this.requireContext(), viewModel), Affordances.FULL)
            "AdvancedGlslUBO" -> setupRenderer(Renderer480AdvancedGlslUBO(this.requireContext(), viewModel), Affordances.FULL)
            "InstancingQuads" -> setupRenderer(Renderer4101InstancingQuads(this.requireContext(), viewModel), Affordances.NONE)
            "InstancingHacking" -> setupRenderer(Renderer4102InstancingHacking(this.requireContext(), viewModel), Affordances.NONE)
            "AsteroidsInstanced" -> setupRenderer(Renderer4103AdvancedAsteroidsInstanced(this.requireContext(), viewModel), Affordances.FULL)
            else -> {
                setupRenderer(GoogleSampleRenderer(this.requireContext(), viewModel), Affordances.SPIN)
            }
        }

        // Render the view only when there is a change in the drawing data
        // doesn't seem to have much effect now on CPU
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        return binding.root
    }

    private fun setupRenderer(rendererIn: RendererBaseClass, mouseAffordance: Affordances) {
        val renderer = rendererIn as GLSurfaceView.Renderer
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(renderer)

        val displayMetrics = requireContext().resources.displayMetrics
        //requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        glSurfaceView.setRendererInInstance(rendererIn, displayMetrics.density, viewModel)

        /*
         * use of SpannableStringBuilder
         * https://stackoverflow.com/a/50053818
         */

        // example
//        val s = SpannableStringBuilder()
//                .append("First Part Not Bold ")
//                .bold { append("BOLD") }
//                .append("Rest not bold")

        val whatIsSupported = when (mouseAffordance) {
            Affordances.NONE -> SpannableStringBuilder().bold{append("Exercise has no touch support")}
            Affordances.SPIN -> SpannableStringBuilder().bold{append("Exercise has spin support")}
            Affordances.FULL -> SpannableStringBuilder().bold{append("Exercise has touch support for camera motion")}
        }
        Toast.makeText(context, whatIsSupported, Toast.LENGTH_SHORT).show()
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