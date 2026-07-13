package com.samuel.finances.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PaymentMethodApi {

    @GET("api/payment-methods")
    suspend fun getPaymentMethods(): Response<List<PaymentMethodResponse>>

    @GET("api/payment-methods/{id}")
    suspend fun getPaymentMethodById(@Path("id") id: Long): Response<PaymentMethodResponse>
}