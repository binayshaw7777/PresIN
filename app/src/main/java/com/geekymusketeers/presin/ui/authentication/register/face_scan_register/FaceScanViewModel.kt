package com.geekymusketeers.presin.ui.authentication.register.face_scan_register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.models.User
import com.geekymusketeers.presin.models.UserAuthResponse
import com.geekymusketeers.presin.network.onError
import com.geekymusketeers.presin.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FaceScanViewModel(application: Application) : BaseViewModel(application) {

    private val faceScanRepository = FaceScanRepository(application)
    private val registerFaceLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isValidFace: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val progressBarLiveData : MutableLiveData<Int> by lazy { MutableLiveData() }
    val enableFaceScanButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val enableFaceAddButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val userLiveData: MutableLiveData<User> by lazy { MutableLiveData() }
    val userRegisterResponseLiveData: MutableLiveData<UserAuthResponse> by lazy { MutableLiveData() }


    private val faceScanValue = 25

    fun setFaceVisible(value: Boolean) {
        enableFaceAddButtonLiveData.value = value
        updateAddButtonState()
    }

    fun addFace(value: Boolean) {
        isValidFace.value = value
        updateSubmitButtonState()
    }

    fun registerFace(stringJson: String, user: User, timeStamp: Long) {
        registerFaceLiveData.value = stringJson
        user.faceEmbeddings = stringJson
        user.createdAt = timeStamp.toString()
        userLiveData.value = user

        viewModelScope.launch(Dispatchers.IO){
            val response = processCoroutine({ faceScanRepository.registerUser(user) })
            response.onSuccess {
                userRegisterResponseLiveData.postValue(it)
            }
            .onError {
                errorLiveData.postValue(it)
            }
        }
    }

    private fun updateSubmitButtonState() {
        enableFaceScanButtonLiveData.value = isValidFace.value
    }

    private fun updateAddButtonState() {
        enableFaceAddButtonLiveData.value = enableFaceAddButtonLiveData.value
        setProgressBarValue()
    }


    private fun setProgressBarValue() {
        progressBarLiveData.value = 75
        if (registerFaceLiveData.value.isNullOrEmpty().not()){
            progressBarLiveData.value = progressBarLiveData.value!!.plus(faceScanValue)
        }
    }
}