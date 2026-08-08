package com.samuel.finances.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.samuel.finances.repository.MockPaymentMethodRepository

class PaymentMethodViewModel : ViewModel() {

    private val repository = MockPaymentMethodRepository

    val paymentMethods = repository.getMethods()

    var selectedPaymentMethod by mutableStateOf("")
        private set

    fun updatePaymentMethod(newPaymentMethod: String) {
        selectedPaymentMethod = newPaymentMethod
    }
}