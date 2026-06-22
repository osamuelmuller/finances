package com.samuel.finances.model

data class Category(
    val id: Int,
    val name: String,
    val initialBudget: Double,
    val remainingBudget: Double
)
