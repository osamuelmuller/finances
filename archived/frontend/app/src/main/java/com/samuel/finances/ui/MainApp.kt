package com.samuel.finances.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.samuel.finances.components.BottomBar
import com.samuel.finances.navigation.FinanceNavGraph

@Composable
fun MainApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding -> Box(modifier = Modifier.padding(padding)) {
        FinanceNavGraph(navController)}
    }
}