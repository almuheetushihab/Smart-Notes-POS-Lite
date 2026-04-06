package com.shihab.smartnotesposlite.ui.screen.possell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shihab.smartnotesposlite.data.models.CartItem
import com.shihab.smartnotesposlite.data.models.Product
import com.shihab.smartnotesposlite.ui.navigation.Screen
import com.shihab.smartnotesposlite.ui.screen.history.HistoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(viewModel: PosViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Inventory) }
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Smart POS Lite", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                Screen.items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = {
                            if (screen is Screen.Sell) {
                                BadgedBox(badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge { Text(cartItems.size.toString()) }
                                    }
                                }) {
                                    Icon(screen.icon, contentDescription = null)
                                }
                            } else {
                                Icon(screen.icon, contentDescription = null)
                            }
                        },
                        label = { Text(screen.title) },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                is Screen.Inventory -> InventoryTab(viewModel)
                is Screen.Sell -> SellTab(viewModel)
                is Screen.History -> HistoryScreen(viewModel)
            }
        }
    }
}

@Composable
fun InventoryTab(viewModel: PosViewModel) {
    val productList by viewModel.productList.collectAsState()
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var editingProductId by remember { mutableStateOf<Long?>(null) }

    val filteredList = productList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (editingProductId == null) "Add Product" else "Edit Product",
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = productPrice,
                    onValueChange = { productPrice = it },
                    label = { Text("Price (৳)") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.saveProduct(editingProductId, productName, productPrice)
                        productName = ""
                        productPrice = ""
                        editingProductId = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(if (editingProductId == null) "Save Product" else "Update Product")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Inventory...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(25.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredList) { product ->
                ProductItemRow(
                    product,
                    onEdit = {
                        productName = product.name
                        productPrice = product.price.toString()
                        editingProductId = product.id
                    },
                    onDelete = { viewModel.deleteProduct(product) },
                )
            }
        }
    }
}

@Composable
fun SellTab(viewModel: PosViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val productList by viewModel.productList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Billing Section", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total Amount", style = MaterialTheme.typography.bodyMedium)
                    Text("৳ ${String.format("%.2f", totalAmount)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                Button(onClick = { viewModel.completeCheckout() }, enabled = cartItems.isNotEmpty()) {
                    Text("Checkout")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search to Add...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
            shape = RoundedCornerShape(25.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Text("Cart Items", fontWeight = FontWeight.Bold) }
                items(cartItems) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.product.name, fontWeight = FontWeight.Bold)
                                Text("${item.quantity} x ৳${item.product.price}")
                            }
                            IconButton(onClick = { viewModel.removeFromCart(item.product) }) {
                                Icon(Icons.Default.Clear, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Text("Quick Add", fontWeight = FontWeight.Bold) }
                val filtered = productList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                items(filtered) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { viewModel.addToCart(product) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("৳${product.price}", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemRow(product: Product, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onEdit() }, elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, fontWeight = FontWeight.Bold)
                Text(text = "৳ ${product.price}", color = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
