package com.samuel.finances.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.samuel.finances.model.Category
import com.samuel.finances.ui.theme.Sizes
import com.samuel.finances.util.toBrazilianCurrency

@Composable
fun CategoryCard(category: Category) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Sizes.cardSpacing),
        elevation = CardDefaults.cardElevation(Sizes.cardElevation)
    ) {
        Column(
            modifier = Modifier
                .padding(Sizes.screenPadding)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(Sizes.componentSpacing))

            Text(
                text = "Initial Budget: ${category.initialBudget.toBrazilianCurrency()}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Remaining Budget: ${category.remainingBudget.toBrazilianCurrency()}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(Sizes.componentSpacing))

            //Progress indicator
            val progress = category.remainingBudget / category.initialBudget

            val progressColor =
                when {
                    progress > 0.6 -> Color(0xFF4CAF50)
                    progress > 0.3 -> Color(0xFFFFC107)
                    else -> Color (0xFFF44336)
                }
            
            LinearProgressIndicator(
            progress = { progress.toFloat().coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = progressColor
            )
        }
    }
}