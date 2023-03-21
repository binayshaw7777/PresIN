package com.geekymusketeers.presin.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.utils.Logger.debugLog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


fun Context.showToast(message: String, shortToast: Boolean = false) {
    Toast.makeText(this, message, if (shortToast) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}

fun View.setNonDuplicateClickListener(listener: View.OnClickListener?) {
    setOnClickListener {
        var lastClickTime: Long = 0
        if (getTag(R.id.TAG_CLICK_TIME) != null) {
            lastClickTime = getTag(R.id.TAG_CLICK_TIME) as Long
        }
        val curTime = System.currentTimeMillis()
        if (curTime - lastClickTime > context.resources.getInteger(R.integer.duplicate_click_delay)) {
            listener?.onClick(this)
            setTag(R.id.TAG_CLICK_TIME, curTime)
        }
    }
}

fun View.requestFocusAndShowKeyboard(inputMethodManager: InputMethodManager) {
    this.requestFocus()
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun TextView.setStringWithColors(
    text1: String,
    text2: String,
    color1: Int,
    color2: Int
) {
    val spannable = SpannableString(text1 + text2)
    spannable.setSpan(
        ForegroundColorSpan(color1),
        0,
        text1.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        ForegroundColorSpan(color2),
        text1.length,
        text2.length + text1.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = spannable
}

fun Any?.isNull() = this == null

fun TextView.setStringWithColor(text: String, color: Int) {
    val spannable = SpannableString(text)
    spannable.setSpan(
        ForegroundColorSpan(color),
        0,
        text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannable
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, it, length).show()
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
    } else {
        try {
            val activeNetworkInfo = manager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            e.debugLog()
        }
    }
    return false
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

@Suppress("DEPRECATION")
val Context.screenSize: Point
    get() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)

    /*
    Usage:
    openActivity(MyActivity::class.java) {
    putString("string.key", "string.value")
    putInt("string.key", 43) }
    */
}

//fun Context.createBottomSheet(): BottomSheetDialog {
//    return BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
//}
//
//fun Activity.createBottomSheet(): BottomSheetDialog {
//    return BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
//}

fun View.setBottomSheet(bottomSheet: BottomSheetDialog) {
    bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    bottomSheet.setContentView(this)
    bottomSheet.create()
    bottomSheet.show()
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    val date = Date()
    return dateFormat.format(date)
}



/**
 * An inline function to put the value into the shared preferences with their respective key.
 */
inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    edit() {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            else -> {
                throw UnsupportedOperationException("SharedPreferences put() not support ${T::class.qualifiedName.toString()} type of data.")
            }
        }
    }
}