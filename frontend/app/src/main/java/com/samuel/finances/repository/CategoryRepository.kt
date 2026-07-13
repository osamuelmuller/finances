package com.samuel.finances.repository

import com.samuel.finances.api.RetrofitClient

class CategoryRepository {

    private val api = RetrofitClient.categoryApi

    suspend fun getCategories() = api.getCategories()

    suspend fun getCategoryById(id: Long) = api.getCategoryById(id)

}