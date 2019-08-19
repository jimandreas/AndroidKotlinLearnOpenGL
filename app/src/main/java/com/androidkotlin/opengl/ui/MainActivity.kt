package com.androidkotlin.opengl.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.androidkotlin.opengl.ui.databinding.ActivityMainBinding

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
//        supportActionBar!!.title = "asdfasdf"

//        getSupportActionBar()!!.setTitle("Select Image");
//        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

    }

    override fun onNavigateUp(): Boolean {
        val result = Navigation.findNavController(this, R.id.myNavHostFragment).navigateUp()
        return super.onSupportNavigateUp()
    }

}