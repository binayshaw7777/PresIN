package com.geekymusketeers.presin.utils

import android.text.TextUtils


fun String.validateEmail() : Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches();
}

fun String.validatePassword() : Boolean {
    if (this.length < 8) return false
    if (this.firstOrNull { it.isDigit() } == null) return false
    if (this.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return false
    if (this.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) return false
    if (this.firstOrNull { !it.isLetterOrDigit() } == null) return false

    return true
}