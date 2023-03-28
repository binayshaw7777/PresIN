package com.geekymusketeers.presin.models


enum class Admin {
    IS_NOT_ADMIN,
    IS_ADMIN;

    companion object {
        fun fromBoolean(isAdmin: Boolean): Admin {
            return if (isAdmin) IS_ADMIN else IS_NOT_ADMIN
        }

        fun fromItemString(isAdmin: String): Boolean {
            return isAdmin == "Yes, I'm an Admin"
        }
    }

    fun toBoolean(): Boolean {
        return this == IS_ADMIN
    }

    fun toItemString(): String {
        return if (this == IS_ADMIN) {
            "Yes, I'm an Admin"
        } else "No, I'm not an Admin"
    }
}