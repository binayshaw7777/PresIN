package com.geekymusketeers.presin.ui.authentication.forgot_password.verify_otp

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.presin.base.BaseViewModel
import com.geekymusketeers.presin.utils.isNull

class VerifyOTPViewModel(application: Application) : BaseViewModel(application) {
    private val otpLiveData: MutableLiveData<Int> by lazy { MutableLiveData() }
    val isOtpCorrect: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val enableSubmitButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun setOTP(otp: String) {
        if (otp.isNotEmpty()) {
            otpLiveData.value = otp.toInt()
        }
        updateSubmitButtonState()
    }

    fun verifyOTP(sentOTP: Int) {
        val isMatch = sentOTP == otpLiveData.value
        isOtpCorrect.postValue(isMatch)
    }

    private fun updateSubmitButtonState() {
        val otp = otpLiveData.value
        val otpIsNotNull = otp.isNull().not()

        if (otpIsNotNull && otp in 1000..9999) {
            enableSubmitButtonLiveData.value = true
            return
        }

        enableSubmitButtonLiveData.value = false
    }
}