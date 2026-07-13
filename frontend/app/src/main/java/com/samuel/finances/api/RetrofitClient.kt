package com.samuel.finances.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.15:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val categoryApi: CategoryApi by lazy {
        retrofit.create(CategoryApi::class.java)
    }

    val paymentMethodApi: PaymentMethodApi by lazy {
        retrofit.create(PaymentMethodApi::class.java)
    }

    val purchaseApi: PurchaseApi by lazy {
        retrofit.create(PurchaseApi::class.java)
    }

}
