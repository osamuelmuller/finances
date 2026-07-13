package com.samuel.finances.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PurchaseApi {

    @GET("api/purchases")
    suspend fun getPurchases(): Response<List<PurchaseResponse>>

    @GET("api/purchases/{id}")
    suspend fun getPurchaseById(@Path("id") id: Long): Response<PurchaseResponse>
}