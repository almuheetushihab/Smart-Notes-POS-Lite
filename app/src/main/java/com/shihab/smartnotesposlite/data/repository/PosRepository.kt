package com.shihab.smartnotesposlite.data.repository

import com.shihab.smartnotesposlite.data.local.ProductDao
import com.shihab.smartnotesposlite.data.local.SaleDao
import com.shihab.smartnotesposlite.data.models.Product
import com.shihab.smartnotesposlite.data.models.Sale
import com.shihab.smartnotesposlite.data.models.SaleItem
import com.shihab.smartnotesposlite.data.models.SaleWithItems
import kotlinx.coroutines.flow.Flow

class PosRepository(
    private val productDao: ProductDao,
    private val saleDao: SaleDao
) {

    // Product related
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

    // Sale related
    val allSales: Flow<List<SaleWithItems>> = saleDao.getAllSales()

    suspend fun completeSale(sale: Sale, items: List<SaleItem>) {
        saleDao.completeSale(sale, items)
    }
}
