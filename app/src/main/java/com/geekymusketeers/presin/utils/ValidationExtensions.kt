package com.geekymusketeers.presin.utils


import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView

private fun isValidPassword(password: String): Boolean {
    if (password.length < 8) return false
    if (password.firstOrNull { it.isDigit() } == null) return false
    if (password.filter { it.isLetter() }
            .firstOrNull { it.isUpperCase() } == null) return false
    if (password.filter { it.isLetter() }
            .firstOrNull { it.isLowerCase() } == null) return false
    if (password.firstOrNull { !it.isLetterOrDigit() } == null) return false

    return true
}

fun ImageView.togglePasswordVisibility(editText: EditText, passwordVisible: Boolean, showPasswordResId: Int, hidePasswordResId: Int) {
    val currentTransformationMethod = editText.transformationMethod
    val newTransformationMethod = if (passwordVisible) null else PasswordTransformationMethod()
    setImageResource(if (passwordVisible) showPasswordResId else hidePasswordResId)
    editText.text = editText.text
    editText.setSelection(editText.text?.length ?: 0)
    if (currentTransformationMethod != newTransformationMethod) {
        editText.transformationMethod = newTransformationMethod
    }
}