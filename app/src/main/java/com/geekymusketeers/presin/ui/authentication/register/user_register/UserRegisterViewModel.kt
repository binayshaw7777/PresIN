package com.geekymusketeers.presin.ui.authentication.register.user_register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.models.User
import com.geekymusketeers.presin.utils.Validator.Companion.isValidEmail
import com.geekymusketeers.presin.utils.Validator.Companion.isValidName
import com.geekymusketeers.presin.utils.Validator.Companion.isValidPassword
import com.geekymusketeers.presin.utils.Validator.Companion.isValidPhone

class UserRegisterViewModel(application: Application) : BaseViewModel(application) {

    private val registerNameLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerEmailLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerPhoneNumberLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    private val registerPasswordLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isValidName: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidEmail: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidPhoneNumber: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidPassword: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val userLiveData : MutableLiveData<User> by lazy { MutableLiveData() }
    val progressBarLiveData : MutableLiveData<Int> by lazy { MutableLiveData() }
    val enableUserRegisterButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }


    private val nameValue = 6
    private val emailValue = 6
    private val phoneNumberValue = 6
    private val passwordValue = 7


    fun registerName(name: String) {
        registerNameLiveData.value = name
        updateButtonState()
    }

    fun registerEmail(email: String) {
        registerEmailLiveData.value = email
        updateButtonState()
    }

    fun registerPhone(phone: String) {
        registerPhoneNumberLiveData.value = phone
        updateButtonState()
    }

    fun registerPassword(password: String) {
        registerPasswordLiveData.value = password
        updateButtonState()
    }

    fun userRegistration() {

        val name: String = registerNameLiveData.value.toString().trim()
        val email: String = registerEmailLiveData.value.toString().trim().lowercase()
        val phone: String = registerPhoneNumberLiveData.value.toString().trim()
        val password: String = registerPasswordLiveData.value.toString().trim()


        if (name.isValidName().not()) {
            isValidName.postValue(false)
            return
        }
        if (email.isValidEmail().not()) {
            isValidEmail.postValue(false)
            return
        }
        if (phone.isValidPhone().not()) {
            isValidPhoneNumber.postValue(false)
            return
        }
        if (password.isValidPassword().not()) {
            isValidPassword.postValue(false)
            return
        }
        val user = User(
            name = name,
            email = email,
            phone = phone,
            password = password,
            role = "",
            organization = "",
            faceEmbeddings = "",
        )
        userLiveData.postValue(user)
    }

    private fun updateButtonState() {
        val requiredFields = registerNameLiveData.value.isNullOrEmpty() ||
                registerEmailLiveData.value.isNullOrEmpty() ||
                registerPhoneNumberLiveData.value.isNullOrEmpty() ||
                registerPasswordLiveData.value.isNullOrEmpty()

        enableUserRegisterButtonLiveData.value = requiredFields.not()
        setProgressValue()
    }

    private fun setProgressValue() {
        progressBarLiveData.value = 0
        if (registerNameLiveData.value?.isValidName() == true){
            progressBarLiveData.value = progressBarLiveData.value!!.plus(nameValue)
        }
        if (registerEmailLiveData.value?.isValidEmail() == true){
            progressBarLiveData.value = progressBarLiveData.value!!.plus(emailValue)
        }
        if (registerPhoneNumberLiveData.value?.isValidPhone() == true){
            progressBarLiveData.value = progressBarLiveData.value!!.plus(phoneNumberValue)
        }
        if (registerPasswordLiveData.value?.isValidPassword() == true){
            progressBarLiveData.value = progressBarLiveData.value!!.plus(passwordValue)
        }
    }
}