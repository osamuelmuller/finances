package com.samuel.finances.repository

import com.samuel.finances.api.RetrofitClient

class PaymentMethodRepository {

    private val api = RetrofitClient.paymentMethodApi

    suspend fun getPaymentMethods() = api.getPaymentMethods()

    suspend fun getPaymentMethodById(id: Long) = api.getPaymentMethodById(id)

}