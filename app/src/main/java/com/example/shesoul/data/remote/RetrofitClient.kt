package com.example.shesoul.data.remote

import com.example.shesoul.data.auth.AuthInterceptor
import com.example.shesoul.data.auth.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context

object RetrofitClient {
    private const val BASE_URL = "https://shesoul-backend.onrender.com/"

    fun create(context: Context): ApiService {
        val tokenManager = TokenManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
