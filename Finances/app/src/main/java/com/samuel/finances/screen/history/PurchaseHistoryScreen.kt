package com.samuel.finances.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samuel.finances.components.FilterSelection
import com.samuel.finances.components.PurchaseCard
import com.samuel.finances.repository.MockCategoryRepository
import com.samuel.finances.repository.MockPaymentMethodRepository
import com.samuel.finances.repository.MockPurchaseRepository

@Composable
fun PurchaseHistoryScreen() {
    val purchase = MockPurchaseRepository.getPurchases()

    val categories = listOf(
        "All"
    ) + MockCategoryRepository
        .getCategories()
        .map { it.name }

    val paymentMethods = listOf(
        "All"
    ) + MockPaymentMethodRepository
        .getMethods()
        .map { it.name }

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    var selectedPaymentMethod by remember {
        mutableStateOf("All")
    }

    var selectedDate by remember {
        mutableStateOf("Current Month")
    }

    val dateOptions = listOf(
        "Current Month",
        "Last 30 Days",
        "Last 90 Days",
        "This Year",
        "Custom"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Purchase History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        FilterSelection(
            selectedDate = selectedDate,
            selectedCategory = selectedCategory,
            selectedPaymentMethod = selectedPaymentMethod,
            dateOptions = dateOptions,
            categoryOptions = categories,
            paymentMethodOptions = paymentMethods,
            onDateSelected = {
                selectedDate = it
            },
            onCategorySelected = {
                selectedCategory = it
            },
            onPaymentMethodSelected = {
                selectedPaymentMethod = it
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(purchase) { purchase ->
                PurchaseCard(purchase = purchase)
            }
        }
    }
}