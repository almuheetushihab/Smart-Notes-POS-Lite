package com.shihab.smartnotesposlite.ui.screen.possell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shihab.smartnotesposlite.data.models.CartItem
import com.shihab.smartnotesposlite.data.models.Product
import com.shihab.smartnotesposlite.data.models.Sale
import com.shihab.smartnotesposlite.data.models.SaleItem
import com.shihab.smartnotesposlite.data.models.SaleWithItems
import com.shihab.smartnotesposlite.data.repository.PosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PosViewModel(private val repository: PosRepository) : ViewModel() {

    // Products from repository
    val productList: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Sales history
    val salesHistory: StateFlow<List<SaleWithItems>> = repository.allSales
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Cart state
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Total price calculation
    val totalAmount: StateFlow<Double> = _cartItems.map { items ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // --- Product CRUD ---

    fun saveProduct(id: Long?, name: String, priceString: String) {
        val price = priceString.toDoubleOrNull() ?: 0.0
        if (name.isNotEmpty() && price > 0) {
            viewModelScope.launch {
                if (id == null) {
                    val newProduct = Product(
                        id = System.currentTimeMillis(),
                        name = name,
                        price = price
                    )
                    repository.addProduct(newProduct)
                } else {
                    val updatedProduct = Product(id ?: 0, name, price)
                    repository.updateProduct(updatedProduct)
                }
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product.id)
            removeFromCart(product)
        }
    }

    // --- Cart Logic ---

    fun addToCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(product, 1)
            }
        }
    }

    fun removeFromCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                if (existingItem.quantity > 1) {
                    currentItems.map {
                        if (it.product.id == product.id) it.copy(quantity = it.quantity - 1) else it
                    }
                } else {
                    currentItems.filterNot { it.product.id == product.id }
                }
            } else {
                currentItems
            }
        }
    }

    fun completeCheckout() {
        val currentCart = _cartItems.value
        if (currentCart.isEmpty()) return

        viewModelScope.launch {
            val total = currentCart.sumOf { it.totalPrice }
            val sale = Sale(totalAmount = total)
            val saleItems = currentCart.map { cartItem ->
                SaleItem(
                    saleId = 0, // Will be set by SaleDao.completeSale
                    productId = cartItem.product.id,
                    productName = cartItem.product.name,
                    price = cartItem.product.price,
                    quantity = cartItem.quantity
                )
            }
            repository.completeSale(sale, saleItems)
            clearCart()
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    class PosViewModelFactory(private val repository: PosRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PosViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
