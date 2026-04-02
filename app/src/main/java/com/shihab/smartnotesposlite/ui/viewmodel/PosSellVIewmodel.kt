package com.shihab.smartnotesposlite.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shihab.smartnotesposlite.data.models.Product
import com.shihab.smartnotesposlite.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PosViewModel(private val repository: ProductRepository) : ViewModel() {

    val productList: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveNewProduct(name: String, priceString: String) {
        val price = priceString.toDoubleOrNull() ?: 0.0
        if (name.isNotEmpty() && price > 0) {
            viewModelScope.launch {
                val newProduct = Product(
                    id = System.currentTimeMillis(),
                    name = name,
                    price = price
                )
                repository.addProduct(newProduct)
            }
        }
    }

    class PosViewModelFactory(private val repository: ProductRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PosViewModel(repository) as T
        }
    }
}