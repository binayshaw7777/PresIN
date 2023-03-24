package com.geekymusketeers.presin.ui.authentication.register.avatar_register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel

class AvatarRegisterViewModel(application: Application): BaseViewModel(application) {

    private val registerAvatarLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    val isValidAvatar: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val enableAvatarRegisterButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun registerAvatar(avatar: String){
        registerAvatarLiveData.value = avatar
        updateButtonText()
    }

    private fun updateButtonText() {
        val isNext = registerAvatarLiveData.value.isNullOrEmpty()
        enableAvatarRegisterButtonLiveData.value = isNext.not()

    }
}