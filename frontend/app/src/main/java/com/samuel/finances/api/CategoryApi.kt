package com.samuel.finances.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApi {

    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    @GET("api/categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Long): Response<CategoryResponse>
}
