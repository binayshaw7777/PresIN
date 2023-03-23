package com.geekymusketeers.presin.ui.authentication.forgot_password.set_new_password

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.network.onError
import com.geekymusketeers.presin.network.onSuccess
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.SetNewPasswordRequest
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.SetNewPasswordResponse
import com.geekymusketeers.presin.utils.Validator.Companion.isValidPassword
import com.geekymusketeers.presin.utils.isNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SetNewPasswordViewModel(application: Application) : BaseViewModel(application) {

    private val repository = SetNewPasswordRepository(application)
    private val passwordLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isPasswordValid: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val setNewPasswordResponse: MutableLiveData<SetNewPasswordResponse> by lazy { MutableLiveData() }
    val enableSubmitButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }


    fun setPassword(password: String) {
        passwordLiveData.value = password
        updateSubmitButtonState()
    }

    fun setNewPassword(userId: String, token: String) {
        val password = passwordLiveData.value.toString()
        if (password.isValidPassword().not()) {
            isPasswordValid.postValue(false)
            return
        }

        val request = SetNewPasswordRequest(password)

        viewModelScope.launch(Dispatchers.IO) {
            val response = processCoroutine({ repository.setNewPassword(userId, token, request) })
            response.onSuccess {
                setNewPasswordResponse.postValue(it)
            }.onError {
                errorLiveData.postValue(it)
            }
        }
    }

    private fun updateSubmitButtonState() {
        enableSubmitButtonLiveData.value = passwordLiveData.value.isNull().not()
    }
}