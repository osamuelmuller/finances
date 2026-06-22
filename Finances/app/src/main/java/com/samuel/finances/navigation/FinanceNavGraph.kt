package com.samuel.finances.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samuel.finances.screen.addpurchase.AddPurchaseScreen
import com.samuel.finances.screen.categories.CategoriesScreen
import com.samuel.finances.screen.history.PurchaseHistoryScreen

@Composable
fun FinanceNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Categories.route
    ) {
        composable(Screen.Categories.route) {
            CategoriesScreen()
        }

        composable(Screen.AddPurchase.route) {
            AddPurchaseScreen()
        }

        composable(Screen.History.route) {
            PurchaseHistoryScreen()
        }
    }
}