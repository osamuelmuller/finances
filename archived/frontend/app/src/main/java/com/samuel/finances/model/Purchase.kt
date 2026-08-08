package com.samuel.finances.model

import java.time.LocalDate

data class Purchase(
    val id: Int,
    val description: String,
    val category: Category,
    val paymentMethod: PaymentMethod,
    val value: Double,
    val date: LocalDate
)
