package com.geekymusketeers.presin.ui.authentication.register.face_scan_register

import android.content.Context
import com.geekymusketeers.presin.models.User
import com.geekymusketeers.presin.network.BaseRepository
import com.geekymusketeers.presin.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FaceScanRepository(context: Context) : BaseRepository(context) {

    private val apiService = getRetrofit().create(ApiService::class.java)

    suspend fun registerUser(user: User) = withContext(Dispatchers.IO) {
        apiService.registerUser(user)
    }
}