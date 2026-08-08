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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samuel.finances.components.FilterSelection
import com.samuel.finances.components.PurchaseCard
import com.samuel.finances.repository.MockCategoryRepository
import com.samuel.finances.repository.MockPaymentMethodRepository
import com.samuel.finances.repository.MockPurchaseRepository
import com.samuel.finances.ui.theme.Sizes
import com.samuel.finances.viewmodel.CategoriesViewModel
import com.samuel.finances.viewmodel.PaymentMethodViewModel
import com.samuel.finances.viewmodel.PurchaseHistoryViewModel

@Composable
fun PurchaseHistoryScreen() {
    val purchaseHistoryViewModel: PurchaseHistoryViewModel = viewModel()
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val paymentMethodViewModel: PaymentMethodViewModel = viewModel()

    val categories = listOf(
        "All"
    ) + categoriesViewModel.categories.map { it.name }

    val paymentMethods = listOf(
        "All"
    ) + paymentMethodViewModel.paymentMethods.map { it.name }

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
            .padding(Sizes.screenPadding)
    ) {
        Text(
            text = "Purchase History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(Sizes.spacers))

        FilterSelection(
            selectedDate = selectedDate,
            selectedCategory = categoriesViewModel.selectedCategory,
            selectedPaymentMethod = paymentMethodViewModel.selectedPaymentMethod,
            dateOptions = dateOptions,
            categoryOptions = categories,
            paymentMethodOptions = paymentMethods,
            onDateSelected = {
                selectedDate = it
            },
            onCategorySelected = { categoriesViewModel.updateCategory(it) },
            onPaymentMethodSelected = { paymentMethodViewModel.updatePaymentMethod(it) }
        )

        Spacer(modifier = Modifier.height(Sizes.spacers))

        if (purchaseHistoryViewModel.purchases.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No purchases found")
            }

            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Sizes.cardSpacing),
        ) {
            items(purchaseHistoryViewModel.purchases) { purchases ->
                PurchaseCard(purchase = purchases)
            }
        }
    }
}