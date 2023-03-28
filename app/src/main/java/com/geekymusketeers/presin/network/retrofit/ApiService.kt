package com.geekymusketeers.presin.network.retrofit

import com.geekymusketeers.presin.models.*
import com.geekymusketeers.presin.network.NetworkResponse
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.ForgotPasswordRequest
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.ForgotPasswordResponse
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.SetNewPasswordRequest
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.SetNewPasswordResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    /**   User calls   **/
    @GET("users/all")
    suspend fun getUsers(): NetworkResponse<AllUsersResponse>

    @POST("users/register")
    suspend fun registerUser(@Body user: User): NetworkResponse<UserAuthResponse>

    @POST("users/login")
    suspend fun loginUser(@Body userLoginRequest: UserLoginRequest): NetworkResponse<UserAuthResponse>

    @POST("users/reset-password-otp")
    suspend fun resetPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): NetworkResponse<ForgotPasswordResponse>

    @POST("users/reset-password-otp/{userId}/{token}")
    suspend fun setNewPassword(
        @Path("userId") userId: String,
        @Path("token") token: String,
        @Body setNewPasswordRequest: SetNewPasswordRequest
    ): NetworkResponse<SetNewPasswordResponse>


    /**   Attendance calls   **/
    @GET("attendance/all")
    suspend fun getAllAttendance(): NetworkResponse<AttendanceResponse>


    /**   Organization calls   **/
    @GET("organization/all")
    suspend fun getAllOrganization(): NetworkResponse<GetAllOrganizationResponse>

    @GET("organization/{id}")
    suspend fun getOrganizationById(
        @Path("id") id: String
    ) : NetworkResponse<GetOrganizationByIdResponse>

    @POST("organization/")
    suspend fun addOrganization(
        @Body organization: AddOrganizationRequest
    ) : NetworkResponse<AddOrganizationResponse>


    /**   Role calls   **/
    @POST("roles/addRole")
    suspend fun addRole(@Body role: RoleAddRequest): NetworkResponse<RoleAddResponse>

    @GET("roles/getAllRoles")
    suspend fun getAllRoles(): NetworkResponse<GetAllRoleResponse>


//    @GET("304c90c8-d6bb-470b-b4bf-81c24a930705")
//    suspend fun requestResidentsList(
////        @Query("search") search: String? = null,
////        @Query("filter") filter: String? = null,
////        @Query("offset") offset: Int,
////        @Query("limit") limit: Int
//    ): NetworkResponse<ResidentsResponse>
}