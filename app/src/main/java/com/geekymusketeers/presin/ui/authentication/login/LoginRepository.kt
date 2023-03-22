package com.geekymusketeers.presin.ui.authentication.login

import android.content.Context
import com.geekymusketeers.presin.models.UserLoginRequest
import com.geekymusketeers.presin.network.BaseRepository
import com.geekymusketeers.presin.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginRepository(context: Context) : BaseRepository(context) {

    private val apiService = getRetrofit().create(ApiService::class.java)

    suspend fun loginUser(userLoginRequest: UserLoginRequest) = withContext(Dispatchers.IO) {
        apiService.loginUser(userLoginRequest)
    }

}