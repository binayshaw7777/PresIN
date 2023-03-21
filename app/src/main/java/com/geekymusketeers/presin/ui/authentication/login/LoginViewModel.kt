package com.geekymusketeers.presin.ui.authentication.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel


class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val emailLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val passwordLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val enableSubmitButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun setEmail(email: String) {
        emailLiveData.value = email
        updateSubmitButtonState()
    }

    fun setPassword(password: String) {
        passwordLiveData.value = password
        updateSubmitButtonState()
    }

    private fun updateSubmitButtonState() {
        val anyRequiredFieldEmpty =
            emailLiveData.value.isNullOrEmpty() || passwordLiveData.value.isNullOrEmpty()
        enableSubmitButtonLiveData.value = anyRequiredFieldEmpty.not()
    }
}