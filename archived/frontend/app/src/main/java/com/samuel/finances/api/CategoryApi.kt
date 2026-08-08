package com.samuel.finances.api

import com.samuel.finances.model.Category
import retrofit2.http.GET

interface CategoryApi {

    @GET("api/categories")
    suspend fun getCategories(): List<Category>
}