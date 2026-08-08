package com.samuel.finances.model

import java.time.LocalDate

data class PurchaseResponse(
    val id: Long,
    val description: String,
    val value: Double,
    val date: LocalDate,
    val categoryName: String,
    val paymentMethodName: String
)
