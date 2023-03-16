package com.geekymusketeers.presin.models

import com.google.gson.annotations.SerializedName


data class UserAuthResponseWithToken(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String? = null,
    @SerializedName("user") val user: User?= null
)
