package com.geekymusketeers.presin.network.retrofit

import android.app.Application
import android.content.Context
import com.geekymusketeers.presin.BuildConfig
import com.geekymusketeers.presin.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * This class is responsible to maintain a single instance of Retrofit.
 * To initialize this class do use {@link #getInstance()} method instead of directly calling its primary constructor
 *  * @constructor
 * @param application Application context
 * */
class RetrofitService private constructor(val application: Application) {
    lateinit var retrofitInstance: Retrofit
    private var okHttpClient: OkHttpClient? = null

    init {
        createRetrofit()
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitService? = null

        fun getInstance(context: Context): RetrofitService =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RetrofitService(context.applicationContext as Application)
                    .also {
                        INSTANCE = it
                    }
            }
    }

    private fun createRetrofit(): Retrofit {
        retrofitInstance = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory.create())
            .client(getHttpClient())
            .baseUrl(Constants.BASE_URL)
            .build()
        return retrofitInstance
    }

    private fun getHttpClient(): OkHttpClient {
        okHttpClient?.let {
            return it
        }
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder().method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
            .addInterceptor(getLoggingInterceptor())
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .connectTimeout(120, TimeUnit.SECONDS)
        okHttpClient = okHttpClientBuilder.build()
        return okHttpClient as OkHttpClient
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
}