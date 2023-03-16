package com.geekymusketeers.presin.network.retrofit

import com.geekymusketeers.presin.network.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseCallAdapterFactory: CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // suspend functions wrap the response type in `Call`
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        // get the response type inside the `Call` type
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) !=  NetworkResponse::class.java) {
            return null
        }

        val responseType = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResponseAdapter(responseType)
    }

    companion object {
        fun create() = NetworkResponseCallAdapterFactory()
    }
}