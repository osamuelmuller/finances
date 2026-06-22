package com.samuel.finances.navigation

sealed class Screen(val route : String) {
    object Categories : Screen ("categories")

    object AddPurchase : Screen ("add_purchase")

    object History : Screen ("history")
}