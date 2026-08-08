package com.samuel.finances.model

import java.math.BigDecimal

data class Category(
    val id: Int,
    val name: String,
    val initialBudget: BigDecimal,
    val remainingBudget: BigDecimal
)
