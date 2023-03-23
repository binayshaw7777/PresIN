package com.geekymusketeers.presin.ui.authentication.forgot_password.set_new_password

import android.content.Context
import com.geekymusketeers.presin.network.BaseRepository
import com.geekymusketeers.presin.network.retrofit.ApiService
import com.geekymusketeers.presin.ui.authentication.forgot_password.models.SetNewPasswordRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SetNewPasswordRepository(context: Context) : BaseRepository(context) {

    private val apiService = getRetrofit().create(ApiService::class.java)

    suspend fun setNewPassword(userId: String, token: String, password: SetNewPasswordRequest) = withContext(Dispatchers.IO) {
        apiService.setNewPassword(userId, token, password)
    }

}