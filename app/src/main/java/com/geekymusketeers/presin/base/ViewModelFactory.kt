package com.geekymusketeers.presin.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.geekymusketeers.presin.ui.authentication.forgot_password.email_request.ForgotPasswordViewModel
import com.geekymusketeers.presin.ui.authentication.forgot_password.set_new_password.SetNewPasswordViewModel
import com.geekymusketeers.presin.ui.authentication.forgot_password.verify_otp.VerifyOTPViewModel
import com.geekymusketeers.presin.ui.authentication.login.LoginViewModel
import com.geekymusketeers.presin.ui.authentication.register.avatar_register.AvatarRegisterViewModel
import com.geekymusketeers.presin.ui.authentication.register.face_scan_register.FaceScanViewModel
import com.geekymusketeers.presin.ui.authentication.register.org_register.OrganizationRegisterViewModel
import com.geekymusketeers.presin.ui.authentication.register.user_register.UserRegisterViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            // Get the Application object from extras
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            when {
                isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(application)
                }
                isAssignableFrom(ForgotPasswordViewModel::class.java) -> {
                    ForgotPasswordViewModel(application)
                }
                isAssignableFrom(VerifyOTPViewModel::class.java) -> {
                    VerifyOTPViewModel(application)
                }
                isAssignableFrom(SetNewPasswordViewModel::class.java) -> {
                    SetNewPasswordViewModel(application)
                }
                isAssignableFrom(UserRegisterViewModel::class.java) -> {
                    UserRegisterViewModel(application)
                }
                isAssignableFrom(OrganizationRegisterViewModel::class.java) -> {
                    OrganizationRegisterViewModel(application)
                }
                isAssignableFrom(AvatarRegisterViewModel::class.java) -> {
                    AvatarRegisterViewModel(application)
                }
                isAssignableFrom(FaceScanViewModel::class.java) -> {
                    FaceScanViewModel(application)
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
}