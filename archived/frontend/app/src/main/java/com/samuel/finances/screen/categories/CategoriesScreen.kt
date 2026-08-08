package com.samuel.finances.screen.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.samuel.finances.components.CategoryCard
import com.samuel.finances.ui.theme.Sizes
import com.samuel.finances.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(
    navController: NavController
) {
    val viewModel : CategoriesViewModel = viewModel()
    val categories = viewModel.categories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Sizes.screenPadding)
    ) {
        Text(
            text = "My Categories",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(Sizes.spacers))

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No categories found")
            }

            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Sizes.cardSpacing),
        ) {
            items(categories) { category ->
                CategoryCard(category)
            }
        }
    }
}