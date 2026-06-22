package com.samuel.finances.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            label = "Categories",
            icon = Icons.AutoMirrored.Filled.List,
            route = Screen.Categories.route
        ),
        BottomNavItem(
            label = "Add",
            icon = Icons.Default.Add,
            route = Screen.AddPurchase.route
        ),
        BottomNavItem(
            label = "History",
            icon = Icons.Default.History,
            route = Screen.History.route
        )
    )
}