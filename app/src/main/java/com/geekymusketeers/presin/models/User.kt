package com.geekymusketeers.presin.models

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("isAdmin") var isAdmin: Boolean,
    @SerializedName("role") var role: String,
    @SerializedName("organization") var organization: String,
    @SerializedName("createdAt") var createdAt: String = "",
    @SerializedName("faceEmbeddings") var faceEmbeddings: String,
    @SerializedName("profileAvatar") var profileAvatar: String = "",
    @SerializedName("__v") var _v: Int? = null
)

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
