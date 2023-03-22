package com.geekymusketeers.presin.ui.authentication.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.models.UserAuthResponse
import com.geekymusketeers.presin.models.UserLoginRequest
import com.geekymusketeers.presin.network.onError
import com.geekymusketeers.presin.network.onSuccess
import com.geekymusketeers.presin.utils.Validator.Companion.isValidEmail
import com.geekymusketeers.presin.utils.Validator.Companion.isValidPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val repository: LoginRepository = LoginRepository(application)
    private val emailLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val passwordLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val enableSubmitButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isEmailValid: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isPasswordValid: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val loginResponse: MutableLiveData<UserAuthResponse> by lazy { MutableLiveData() }

    fun setEmail(email: String) {
        emailLiveData.value = email
        updateSubmitButtonState()
    }

    fun setPassword(password: String) {
        passwordLiveData.value = password
        updateSubmitButtonState()
    }

    fun loginUser() {
        val email: String = emailLiveData.value.toString().trim().lowercase()
        val password: String = passwordLiveData.value.toString().trim()

        if (email.isValidEmail().not()) {
            isEmailValid.postValue(false)
            return
        }

        if (password.isValidPassword().not()) {
            isPasswordValid.postValue(false)
            return
        }

        val loginRequest = UserLoginRequest(email, password)
        viewModelScope.launch(Dispatchers.IO) {
            val response = processCoroutine({ repository.loginUser(loginRequest) })
            response.onSuccess {
                loginResponse.postValue(it)
            }.onError {
                errorLiveData.postValue(it)
            }
        }
    }

    private fun updateSubmitButtonState() {
        val anyRequiredFieldEmpty =
            emailLiveData.value.isNullOrEmpty() || passwordLiveData.value.isNullOrEmpty()
        enableSubmitButtonLiveData.value = anyRequiredFieldEmpty.not()
    }
}