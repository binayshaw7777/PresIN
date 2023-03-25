package com.geekymusketeers.presin.ui.authentication.register.avatar_register

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.models.User
import com.geekymusketeers.presin.utils.isNull

class AvatarRegisterViewModel(application: Application): BaseViewModel(application) {

    private val registerAvatarLiveData: MutableLiveData<Uri> by lazy { MutableLiveData() }
    val isValidAvatar: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val userLiveData : MutableLiveData<User> by lazy { MutableLiveData() }
    val enableAvatarRegisterButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun setAvatarUri(avatar: Uri?) {
        avatar?.let {
            registerAvatarLiveData.value = avatar
            enableAvatarRegisterButtonLiveData.value = true
            updateButtonText()
        }
    }

    fun avatarRegister(user: User){

        val avatar = registerAvatarLiveData.value
        if (avatar.isNull()){
            isValidAvatar.postValue(false)
            return
        }
        user.profileAvatar = getAvatarUrl(avatar)
        userLiveData.postValue(user)
    }

    private fun getAvatarUrl(avatar: Uri?): String {
        // Get URL from Firebase
        return ""
    }

    private fun updateButtonText() {
        val isNext = !registerAvatarLiveData.value.isNull()
        enableAvatarRegisterButtonLiveData.value = isNext.not()

    }
}