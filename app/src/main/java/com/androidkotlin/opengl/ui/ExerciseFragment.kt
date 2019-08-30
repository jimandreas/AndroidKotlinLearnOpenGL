package com.androidkotlin.opengl.ui

import android.graphics.Typeface.*
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.text.*
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.androidkotlin.opengl.googlesample.GoogleSampleRenderer
import com.androidkotlin.opengl.learnopengl.*
import com.androidkotlin.opengl.realtime.RendererBaseClass
import com.androidkotlin.opengl.realtime.SurfaceViewInstance
import com.androidkotlin.opengl.ui.databinding.FragmentExerciseBinding
import timber.log.Timber

enum class Affordances { NONE, SPIN, FULL }

class ExerciseFragment : Fragment() {

    private val viewModel: ViewModel by lazy {
        ViewModelProviders.of(this).get(ViewModel::class.java)
    }
    private lateinit var glSurfaceView: SurfaceViewInstance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentExerciseBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.setLocalLifecycleOwner(this)
        binding.viewModel1 = viewModel
        glSurfaceView = binding.learnOpenglSurfaceView

        val act = activity as AppCompatActivity
        val sab = act.supportActionBar

        val whichExercise = ExerciseFragmentArgs.fromBundle(arguments!!).selectedExercise

        if (sab != null) {
            sab.title = whichExercise
            sab.setDisplayHomeAsUpEnabled(true)
            sab.setDisplayShowHomeEnabled(true)
            //sab.setHomeButtonEnabled(true)  // this is currently ignored!!
        }


        //   Ooops this is the system back button!
        //   I need the home button
        // https://stackoverflow.com/questions/56640729/implementing-custom-back-navigation-in-android
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            Timber.e("WOW back pressed!!")
//        }

        when (whichExercise) {
            "GoogleSample" -> setupRenderer(GoogleSampleRenderer(this.context!!, viewModel), Affordances.SPIN)
            "HelloTriangle" -> setupRenderer(Renderer121HelloTriangle(this.context!!, viewModel), Affordances.NONE)
            "CoordinateSystems" -> setupRenderer(Renderer163CoordinateSystems(this.context!!, viewModel), Affordances.NONE)
            "Camera" -> setupRenderer(Renderer174Camera(this.context!!, viewModel), Affordances.FULL)
            "LightingMaps" -> setupRenderer(Renderer242LightingMapsSpecular(this.context!!, viewModel), Affordances.FULL)
            "LightingMaps+Cube" -> setupRenderer(Renderer242LightingMapsPlusCubeObject(this.context!!, viewModel), Affordances.FULL)
            "AdvancedGlslUBO" -> setupRenderer(Renderer480AdvancedGlslUBO(this.context!!, viewModel), Affordances.FULL)
            "InstancingQuads" -> setupRenderer(Renderer4101InstancingQuads(this.context!!, viewModel), Affordances.NONE)
            "InstancingHacking" -> setupRenderer(Renderer4102InstancingHacking(this.context!!, viewModel), Affordances.NONE)
            "AsteroidsInstanced" -> setupRenderer(Renderer4103AdvancedAsteroidsInstanced(this.context!!, viewModel), Affordances.FULL)
            else -> {
                setupRenderer(GoogleSampleRenderer(this.context!!, viewModel), Affordances.SPIN)
            }
        }

        // Render the view only when there is a change in the drawing data
        // doesn't seem to have much effect now on CPU
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Timber.e("Exercise:  back button seen!!")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRenderer(rendererIn: RendererBaseClass, mouseAffordance: Affordances) {
        val renderer = rendererIn as GLSurfaceView.Renderer
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(renderer)

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        glSurfaceView.setRendererInInstance(rendererIn, displayMetrics.density, viewModel)

        /*
         * use of SpannableStringBuilder
         * https://stackoverflow.com/a/50053818
         */
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