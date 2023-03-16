package com.geekymusketeers.presin.models

import com.google.gson.annotations.SerializedName


data class AttendanceResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String? = null,
    @SerializedName("attendanceList") var attendanceList: ArrayList<Attendance> = arrayListOf()
)
