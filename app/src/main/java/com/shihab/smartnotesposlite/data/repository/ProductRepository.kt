package com.shihab.smartnotesposlite.data.repository

import com.shihab.smartnotesposlite.data.local.PosStorageManager
import com.shihab.smartnotesposlite.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProductRepository(private val storageManager: PosStorageManager) {

    val allProducts: Flow<List<Product>> = storageManager.getSavedProducts

    suspend fun addProduct(newProduct: Product) {
        val currentList = allProducts.first().toMutableList()
        currentList.add(newProduct)
        storageManager.saveProducts(currentList)
    }

    suspend fun updateProduct(updatedProduct: Product) {
        val currentList = allProducts.first().toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            currentList[index] = updatedProduct
            storageManager.saveProducts(currentList)
        }
    }

    suspend fun deleteProduct(productId: Long) {
        val currentList = allProducts.first().toMutableList()
        currentList.removeAll { it.id == productId }
        storageManager.saveProducts(currentList)
    }
}
