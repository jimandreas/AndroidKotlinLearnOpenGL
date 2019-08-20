package com.androidkotlin.opengl.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.androidkotlin.opengl.ui.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    /**
     * Our MainActivity is only responsible for setting the content view that contains the
     * Navigation Host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    /*
     * this override handles the back button in the menu bar.
     * The system back button is handled by the navigation system.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                Navigation.findNavController(this, R.id.myNavHostFragment)
                        //.navigateUp())
                        .navigate(ExerciseFragmentDirections.actionExerciseFragmentToSelectorFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}