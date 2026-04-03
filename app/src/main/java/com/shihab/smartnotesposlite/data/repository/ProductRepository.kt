package com.shihab.smartnotesposlite.data.repository

import com.shihab.smartnotesposlite.data.local.ProductDao
import com.shihab.smartnotesposlite.data.models.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun addProduct(newProduct: Product) {
        productDao.insertProduct(newProduct)
    }

    suspend fun updateProduct(updatedProduct: Product) {
        productDao.updateProduct(updatedProduct)
    }

    suspend fun deleteProduct(productId: Long) {
        productDao.deleteProductById(productId)
    }
}
