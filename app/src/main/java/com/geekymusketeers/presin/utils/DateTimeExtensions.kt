package com.geekymusketeers.presin.utils

import com.geekymusketeers.presin.utils.Constants.DATE_FORMAT
import com.geekymusketeers.presin.utils.Constants.TIME_STAMP_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Long.getTimeStamp(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}
fun getTimeStamp(): Long {
    return System.currentTimeMillis()
}

@Throws(ParseException::class)
fun String.getDateUnixTime(): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.parse(this)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}


/*
    ---Usage---
    val currentTime = System.currentTimeMillis()
    println(currentTime.getTimeStamp())
    println(currentTime.getYearMonthDay())
    println("2020-09-20".getDateUnixTime())

    ---Output---
    Sunday, September 20, 2020 - 10:48:26 AM
    2020-09-20
    1600549200000
*/