package com.shihab.smartnotesposlite.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : Screen("inventory", "Inventory", Icons.Default.Add)
    object Sell : Screen("sell", "Sell", Icons.Default.ShoppingCart)
    object History : Screen("history", "History", Icons.Default.History)

    companion object {
        val items = listOf(Inventory, Sell, History)
    }
}
