package com.geekymusketeers.presin.ui.authentication.forgot_password.email_request

import android.content.Context
import com.geekymusketeers.presin.network.BaseRepository
import com.geekymusketeers.presin.network.retrofit.ApiService
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.ForgotPasswordRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ForgotPasswordRepository(context: Context) : BaseRepository(context) {

    private val apiService = getRetrofit().create(ApiService::class.java)

    suspend fun resetPassword(forgotPasswordRequest: ForgotPasswordRequest) = withContext(Dispatchers.IO) {
        apiService.resetPassword(forgotPasswordRequest)
    }


}