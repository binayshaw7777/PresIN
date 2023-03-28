package com.geekymusketeers.presin.ui.authentication.register.org_register

import android.content.Context
import com.geekymusketeers.presin.network.BaseRepository
import com.geekymusketeers.presin.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class OrganizationRegisterRepository(context: Context) : BaseRepository(context) {

    private val apiService = getRetrofit().create(ApiService::class.java)

    suspend fun getAllRoles() = withContext(Dispatchers.IO) {
        apiService.getAllRoles()
    }

    suspend fun getAllOrganization() = withContext(Dispatchers.IO) {
        apiService.getAllOrganization()
    }
}