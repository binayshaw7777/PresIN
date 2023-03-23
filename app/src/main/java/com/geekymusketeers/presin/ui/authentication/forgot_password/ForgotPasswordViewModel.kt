package com.geekymusketeers.presin.ui.authentication.forgot_password

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.network.onError
import com.geekymusketeers.presin.network.onSuccess
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.ForgotPasswordRequest
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.ForgotPasswordResponse
import com.geekymusketeers.presin.utils.Validator.Companion.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {

    private val repository: ForgotPasswordRepository = ForgotPasswordRepository(application)
    val isEmailValid: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val enableSubmitButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    private val emailLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val forgotPasswordResponse: MutableLiveData<ForgotPasswordResponse> by lazy { MutableLiveData() }

    fun setEmail(email: String) {
        emailLiveData.value = email
        updateSubmitButtonState()
    }

    fun requestResetPassword() {
        val email = emailLiveData.value.toString()
        if (email.isValidEmail().not()) {
            isEmailValid.postValue(false)
            return
        }
        val forgotPasswordRequest = ForgotPasswordRequest(email)
        viewModelScope.launch(Dispatchers.IO) {
            val response = processCoroutine({ repository.resetPassword(forgotPasswordRequest) })
            response.onSuccess {
                forgotPasswordResponse.postValue(it)
            }.onError {
                errorLiveData.postValue(it)
            }
        }
    }


    private fun updateSubmitButtonState() {
        enableSubmitButtonLiveData.value = emailLiveData.value.isNullOrEmpty().not()
    }

}