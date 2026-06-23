package com.samuel.finances.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.samuel.finances.model.Category
import com.samuel.finances.model.PaymentMethod
import java.time.LocalDate

@Composable
fun FilterSelection (
    selectedDate: String,
    selectedCategory: String,
    selectedPaymentMethod: String,
    dateOptions: List<String>,
    categoryOptions: List<String>,
    paymentMethodOptions: List<String>,
    onDateSelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onPaymentMethodSelected: (String) -> Unit
    ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DropdownSelector(
            label = "Date Range",
            items = dateOptions,
            selectedItem = selectedDate,
            onItemSelected = onDateSelected
        )

        DropdownSelector(
            label = "Category Selector",
            items = categoryOptions,
            selectedItem = selectedCategory,
            onItemSelected = onCategorySelected
        )

        DropdownSelector(
            label = "Payment Method Selector",
            items = paymentMethodOptions,
            selectedItem = selectedPaymentMethod,
            onItemSelected = onPaymentMethodSelected
        )
    }
}