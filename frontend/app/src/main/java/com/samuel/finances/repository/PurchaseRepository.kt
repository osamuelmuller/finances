package com.samuel.finances.repository

import com.samuel.finances.api.RetrofitClient

class PurchaseRepository {

    private val api = RetrofitClient.purchaseApi

    suspend fun getPurchases() = api.getPurchases()

    suspend fun getPurchaseById(id: Long) = api.getPurchaseById(id)

}