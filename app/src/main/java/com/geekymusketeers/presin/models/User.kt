package com.geekymusketeers.presin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("isAdmin") var isAdmin: Boolean = false,
    @SerializedName("role") var role: String,
    @SerializedName("organization") var organization: String,
    @SerializedName("createdAt") var createdAt: String = "",
    @SerializedName("faceEmbeddings") var faceEmbeddings: String,
    @SerializedName("profileAvatar") var profileAvatar: String = "",
    @SerializedName("__v") var _v: Int? = null
) : Parcelable

data class UserLoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class UserAuthResponse(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String
)

data class AllUsersResponse(
    @SerializedName("message") val message: String,
    @SerializedName("usersList") val listOfUsers: List<User>? = null
)
