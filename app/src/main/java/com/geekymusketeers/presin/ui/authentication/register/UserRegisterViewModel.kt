package com.geekymusketeers.presin.ui.authentication.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel

class UserRegisterViewModel(application: Application) : BaseViewModel(application) {

    private val registerNameLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerEmailLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerPhoneNumberLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerPasswordLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val enableUserRegisterButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun registerName(name : String){
        registerNameLiveData.value = name
        updateButtonState()
    }
    fun registerEmail(email : String){
        registerEmailLiveData.value = email
        updateButtonState()
    }
    fun registerPhone(phone : String){
        registerPhoneNumberLiveData.value = phone
        updateButtonState()
    }
    fun registerPassword(password : String){
        registerPasswordLiveData.value = password
        updateButtonState()
    }

    private fun updateButtonState() {
        val requiredFields = registerNameLiveData.value.isNullOrEmpty() ||
                registerEmailLiveData.value.isNullOrEmpty() ||
                registerPhoneNumberLiveData.value.isNullOrEmpty() ||
                registerPasswordLiveData.value.isNullOrEmpty()
        enableUserRegisterButtonLiveData.value = requiredFields.not()
    }
}