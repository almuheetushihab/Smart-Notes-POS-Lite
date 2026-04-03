package com.shihab.smartnotesposlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shihab.smartnotesposlite.data.local.PosStorageManager
import com.shihab.smartnotesposlite.data.repository.ProductRepository
import com.shihab.smartnotesposlite.ui.theme.SmartNotesPOSLiteTheme
import com.shihab.smartnotesposlite.ui.viewmodel.PosViewModel
import com.shihab.smartnotesposlite.data.models.Product

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storageManager = PosStorageManager(applicationContext)
        val repository = ProductRepository(storageManager)
        val factory = PosViewModel.PosViewModelFactory(repository)

        setContent {
            SmartNotesPOSLiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val viewModel: PosViewModel = viewModel(factory = factory)
                    PosScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun PosScreen(viewModel: PosViewModel) {
    val productList by viewModel.productList.collectAsState()

    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var editingProductId by remember { mutableStateOf<Long?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (editingProductId == null) "Add New Product" else "Edit Product",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    viewModel.saveProduct(editingProductId, productName, productPrice)
                    productName = ""
                    productPrice = ""
                    editingProductId = null
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(if (editingProductId == null) "Save Product" else "Update Product")
            }

            if (editingProductId != null) {
                OutlinedButton(
                    onClick = {
                        productName = ""
                        productPrice = ""
                        editingProductId = null
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Cancel")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Product List:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(productList) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            productName = product.name
                            productPrice = product.price.toString()
                            editingProductId = product.id
                        },
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "৳${product.price}", style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { viewModel.deleteProduct(product) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
