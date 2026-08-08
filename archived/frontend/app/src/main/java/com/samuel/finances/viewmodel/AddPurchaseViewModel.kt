package com.samuel.finances.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.samuel.finances.repository.MockCategoryRepository
import com.samuel.finances.repository.MockPaymentMethodRepository
import java.time.LocalDate

class AddPurchaseViewModel : ViewModel() {
    var description by mutableStateOf("")
        private set

    var value by mutableStateOf("")
        private set

    var date by mutableStateOf(LocalDate.now().toString())
        private set

    fun updateDescription(newDescription: String) {
        this.description = newDescription
    }

    fun updateValue(newValue: String) {
        this.value = newValue
    }
    fun updateDate(newDate: String) {
        this.date = newDate
    }

    val isFormValid: Boolean
        get() = description.isNotBlank() && value.isNotBlank()

    /*
    WARNING!!!!!!!!!
    var selectedCategory and fun updateCategory are in CategoriesViewModel
    and
    var selectedPaymentMethod and fun updatePaymentMethod are in PaymentMethodViewModel
     */
}