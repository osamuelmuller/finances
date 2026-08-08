package com.samuel.finances.repository

import com.samuel.finances.model.Category

object MockCategoryRepository {

    fun getCategories(): List<Category> {
        return listOf(
            Category(1, "Essentials", 2000.0, 1200.0),
            Category(2, "Leisure", 1000.0, 450.0),
            Category(3, "Transport", 800.0, 300.0),
            Category(4, "Emergency Fund", 5000.0, 5000.0),
            Category(5, "Education", 1500.0, 900.0)
        )
    }
}