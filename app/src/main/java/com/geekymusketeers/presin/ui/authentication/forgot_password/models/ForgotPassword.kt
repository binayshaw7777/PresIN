package com.geekymusketeers.presin.ui.authentication.forgot_password.models

import com.google.gson.annotations.SerializedName


data class ForgotPasswordRequest(
    val email: String,
    val otp: Int
)

data class ForgotPasswordResponse(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
    @SerializedName("userID") val userID: String
)

data class SetNewPasswordResponse(
    @SerializedName("message") val message: String
)

data class SetNewPasswordRequest(
    val password: String
)