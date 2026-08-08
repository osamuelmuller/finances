package com.samuel.finances.repository

import com.samuel.finances.model.PaymentMethod

object MockPaymentMethodRepository {

    fun getMethods(): List<PaymentMethod> {
        return listOf(
            PaymentMethod(1, "Santander Debit"),
            PaymentMethod(2, "C6 Credit"),
            PaymentMethod(3, "Cash"),
            PaymentMethod(4, "Food Card")
        )
    }
}