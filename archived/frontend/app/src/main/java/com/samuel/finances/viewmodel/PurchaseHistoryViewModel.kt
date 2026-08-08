package com.samuel.finances.viewmodel

import androidx.lifecycle.ViewModel
import com.samuel.finances.repository.MockPurchaseRepository

class PurchaseHistoryViewModel : ViewModel() {

    private val repository = MockPurchaseRepository

    val purchases = repository.getPurchases()

}