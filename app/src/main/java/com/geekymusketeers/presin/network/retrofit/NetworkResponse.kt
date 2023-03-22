package com.geekymusketeers.presin.network.retrofit

import com.geekymusketeers.presin.network.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResponseAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResponse<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResponse<Type>> {
        return NetworkResponseCall(call)
    }
}