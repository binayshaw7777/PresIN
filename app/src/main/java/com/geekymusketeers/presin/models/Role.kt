package com.geekymusketeers.presin.models

import com.google.gson.annotations.SerializedName


data class Role(
    @SerializedName("_id") val roleId: String? = null,
    @SerializedName("roleName") var roleName: String
)

data class RoleAddRequest(
    val role: Role
)

data class RoleAddResponse(
    @SerializedName("message") val message: String
)

data class GetAllRoleResponse(
    @SerializedName("roles") var roles: ArrayList<Role> = arrayListOf()
)