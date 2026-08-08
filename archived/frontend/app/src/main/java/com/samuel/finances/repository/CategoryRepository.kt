package com.samuel.finances.repository

import com.samuel.finances.api.CategoryApi
import com.samuel.finances.model.Category

class CategoryRepository (private val categoryApi: CategoryApi) {
    suspend fun getCategories(): List<Category> {
        return categoryApi.getCategories()
    }
}