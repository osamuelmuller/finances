package com.samuel.finances.model

data class CategoryResponse(
    val id: Long,
    val name: String,
    val initialBudget: Double,
    val remainingBudget: Double
)