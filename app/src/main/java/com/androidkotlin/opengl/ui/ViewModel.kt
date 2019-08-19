package com.androidkotlin.opengl.ui

import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class ViewModel : ViewModel() {
    lateinit var owner: LifecycleOwner
    fun setLocalLifecycleOwner(ownerIn: LifecycleOwner) {
        owner = ownerIn
    }

    /*
     * LiveData:
     *   wireFrameModeOn - toggled by WIREFRAME/SOLID button in UI
     *   shaderToggle - toggled by SHADER button in UI
     *   renderModelFromBuffer - if TRUE, model is rendered and
     *      retained in OpenGL bound buffers.   This is for the low-res
     *      "opengl" view
     *   lowResViewAdjuster - set by slider in UI - to adjust the level
     *      of detail in the low res "opengl" view
     */
    private val _wireFrameModeOn = MutableLiveData<Boolean>()
    val wireFrameModeOn: LiveData<Boolean>
        get() = _wireFrameModeOn
    private val _shaderToggle = MutableLiveData<Boolean>()
    val shaderToggle: LiveData<Boolean>
        get() = _shaderToggle
    private val _nextModel = MutableLiveData<Int>() // which model
    val nextModel: LiveData<Int>
        get() = _nextModel
    private val _renderModelFromBuffer = MutableLiveData<Boolean>()
    val renderModelFromBuffer: LiveData<Boolean>
        get() = _renderModelFromBuffer
    private val _lowResViewAdjuster = MutableLiveData<Int>()
    val lowResViewAdjuster: LiveData<Int>
        get() = _lowResViewAdjuster

    private val _buttonClicked = MutableLiveData<String>()
    val buttonClicked: LiveData<String>
        get() = _buttonClicked

    fun clickProcessed() {
        _buttonClicked.value = ""
    }
    init {
//        _wireFrameModeOn.value = false
//        _shaderToggle.value = false
//        _nextModel.value = 0
//        _renderModelFromBuffer.value = false
//        _lowResViewAdjuster.value = 5
//        _buttonClicked.value = "none"
    }

    private fun toggleWireFrameMode() {
        _wireFrameModeOn.value = _wireFrameModeOn.value != true
    }
    private fun toggleShaderMode() {
        _shaderToggle.value = _shaderToggle.value != true
    }
    private fun nextModelButtonClick() {
        _nextModel.value = _nextModel.value?.plus(1)

    }

    fun onClickNextModel() {
        _nextModel.value = _nextModel.value?.plus(1)
    }

    fun onClickOpenGL(view: View) {
        Timber.i("Button click id %d", view.id)
    }

    fun onButton(str: String) {
        Timber.i("Button click id %s", str)
        _buttonClicked.value = str
    }

//    private val _seekbarValue: MutableLiveData<Int> = MutableLiveData()
//    val seeekBarValue : LiveData<Int> = _seekbarValue

    fun onProgressChangedHere(seekbar: SeekBar, p: Int, fromUser: Boolean) {
        Timber.i("Seekbar set to %d", p+3)
        _lowResViewAdjuster.value = p+3
    }

}