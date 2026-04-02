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
}