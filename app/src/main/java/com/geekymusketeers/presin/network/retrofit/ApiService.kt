package com.geekymusketeers.presin.network.retrofit

import com.geekymusketeers.presin.models.AttendanceResponse
import com.geekymusketeers.presin.models.User
import com.geekymusketeers.presin.models.UserAuthResponseWithToken
import com.geekymusketeers.presin.network.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("users/all")
    suspend fun getUsers(): NetworkResponse<List<User>>

    @GET("attendance/all")
    suspend fun getAllAttendance(): NetworkResponse<AttendanceResponse>

    @POST("users/register")
    suspend fun registerUser(@Body user: User): NetworkResponse<UserAuthResponseWithToken>

//    @GET("304c90c8-d6bb-470b-b4bf-81c24a930705")
//    suspend fun requestResidentsList(
////        @Query("search") search: String? = null,
////        @Query("filter") filter: String? = null,
////        @Query("offset") offset: Int,
////        @Query("limit") limit: Int
//    ): NetworkResponse<ResidentsResponse>
}