package com.geekymusketeers.presin.models

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("password") var password: String,
    @SerializedName("isAdmin") var isAdmin: Boolean,
    @SerializedName("role") var role: String,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("embeddingsData") var embeddingsData: String,
    @SerializedName("profileAvatar") var profileAvatar: Bitmap,
    @SerializedName("__v") var _v: Int? = null
)
