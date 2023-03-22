package com.geekymusketeers.presin.models

import com.google.gson.annotations.SerializedName


data class Attendance(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("userID") var userID: String,
    @SerializedName("name") var name: String,
    @SerializedName("markedMinute") var markedMinute: Int,
    @SerializedName("markedHour") var markedHour: Int,
    @SerializedName("markedDate") var markedDate: Int,
    @SerializedName("markedMonth") var markedMonth: Int,
    @SerializedName("markedYear") var markedYear: Int,
    @SerializedName("organization") var organization: String,
    @SerializedName("location") var location: String,
    @SerializedName("faceEmbeddings") var faceEmbeddings: String,
    @SerializedName("matchPercentage") var matchPercentage: Int,
    @SerializedName("__v") var _v: Int? = null
)

data class AttendanceResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("attendanceList") var attendanceList: ArrayList<Attendance> = arrayListOf()
)
