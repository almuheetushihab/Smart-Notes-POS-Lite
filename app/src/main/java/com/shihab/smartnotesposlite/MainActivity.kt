package com.shihab.smartnotesposlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shihab.smartnotesposlite.data.local.PosStorageManager
import com.shihab.smartnotesposlite.data.repository.ProductRepository
import com.shihab.smartnotesposlite.ui.theme.SmartNotesPOSLiteTheme
import com.shihab.smartnotesposlite.ui.viewmodel.PosViewModel

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
                    color = MaterialTheme.colorScheme.background
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

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveNewProduct(productName, productPrice)
                productName = ""
                productPrice = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Product")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Saved Products:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(productList) { product ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "৳${product.price}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
