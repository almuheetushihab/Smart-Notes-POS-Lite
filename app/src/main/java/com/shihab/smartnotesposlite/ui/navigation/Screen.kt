package com.shihab.smartnotesposlite.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Inventory : Screen("inventory", "Inventory", Icons.Default.Add)
    data object Sell : Screen("sell", "Sell", Icons.Default.ShoppingCart)
    data object History : Screen("history", "History", Icons.Default.History)

    companion object {
        fun items() = listOf(Inventory, Sell, History)
    }
}
