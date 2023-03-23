package com.geekymusketeers.presin.ui.authentication.forgot_password.models

import com.google.gson.annotations.SerializedName


data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    @SerializedName("message") val message: String
)