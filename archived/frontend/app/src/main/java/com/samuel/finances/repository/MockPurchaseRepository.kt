package com.samuel.finances.repository

import com.samuel.finances.model.Category
import com.samuel.finances.model.PaymentMethod
import com.samuel.finances.model.Purchase
import java.time.LocalDate

object MockPurchaseRepository {

    fun getPurchases(): List<Purchase> {

        return listOf(

            Purchase(
                id = 1,
                description = "Supermarket",
                category = Category(
                    id = 1,
                    name = "Essentials",
                    initialBudget = 2000.0,
                    remainingBudget = 1200.0
                ),
                paymentMethod = PaymentMethod(
                    id = 1,
                    name = "Santander Debit"
                ),
                value = 150.75,
                date = LocalDate.parse("2026-06-26")
            ),

            Purchase(
                id = 2,
                description = "Netflix",
                category = Category(
                    id = 2,
                    name = "Leisure",
                    initialBudget = 1000.0,
                    remainingBudget = 450.0
                ),
                paymentMethod = PaymentMethod(
                    id = 2,
                    name = "C6 Credit"
                ),
                value = 39.90,
                date = LocalDate.parse("2026-06-24")
            ),

            Purchase(
                id = 3,
                description = "Gas Station",
                category = Category(
                    id = 3,
                    name = "Transport",
                    initialBudget = 800.0,
                    remainingBudget = 300.0
                ),
                paymentMethod = PaymentMethod(
                    id = 1,
                    name = "Santander Debit"
                ),
                value = 120.00,
                date = LocalDate.parse("2026-06-22")
            ),

            Purchase(
                id = 4,
                description = "Programming Book",
                category = Category(
                    id = 5,
                    name = "Education",
                    initialBudget = 1500.0,
                    remainingBudget = 900.0
                ),
                paymentMethod = PaymentMethod(
                    id = 2,
                    name = "C6 Credit"
                ),
                value = 89.90,
                date = LocalDate.parse("2026-06-20")
            )
        )
    }
}