package com.samuel.finances.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.samuel.finances.model.Purchase
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PurchaseCard(purchase: Purchase) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val formattedValue = NumberFormat
            .getCurrencyInstance(Locale("pt", "BR"))
            .format(purchase.value)

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = purchase.description,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: ${purchase.category.name}"
            )

            Text(
                text = "Payment: ${purchase.paymentMethod.name}"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Date: ${purchase.date}"
                )

                Text(
                    text = formattedValue,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFD32F2F)
                )
            }
        }
    }

}