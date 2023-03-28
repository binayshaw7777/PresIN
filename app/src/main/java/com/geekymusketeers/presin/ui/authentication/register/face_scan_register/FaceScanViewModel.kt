package com.geekymusketeers.presin.ui.authentication.register.face_scan_register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel

class FaceScanViewModel(application: Application) : BaseViewModel(application) {

    private val registerFaceLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isValidFace: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val progressBarLiveData : MutableLiveData<Int> by lazy { MutableLiveData() }
    val enableFaceScanButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    private val faceScanValue = 25
    fun registerFace(face: String){
        registerFaceLiveData.value = face
        updateButtonState()
    }

    fun fceScanRegister() {

        val face: String = registerFaceLiveData.value.toString().trim().lowercase()

        if (face.isEmpty()) {
            isValidFace.postValue(false)
            return
        }
    }

    private fun updateButtonState() {
        val required = registerFaceLiveData.value.isNullOrEmpty()
        enableFaceScanButtonLiveData.value = required.not()
        setProgressBarValue()
    }

    private fun setProgressBarValue() {
        progressBarLiveData.value = 75
        if (registerFaceLiveData.value.isNullOrEmpty().not()){
            progressBarLiveData.value = progressBarLiveData.value!!.plus(faceScanValue)
        }
    }
}