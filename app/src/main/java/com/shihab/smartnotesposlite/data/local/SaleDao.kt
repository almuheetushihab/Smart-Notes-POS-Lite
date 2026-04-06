package com.shihab.smartnotesposlite.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.shihab.smartnotesposlite.data.models.Sale
import com.shihab.smartnotesposlite.data.models.SaleItem
import com.shihab.smartnotesposlite.data.models.SaleWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    @Transaction
    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<SaleWithItems>>

    @Insert
    suspend fun insertSale(sale: Sale): Long

    @Insert
    suspend fun insertSaleItems(items: List<SaleItem>)

    @Transaction
    suspend fun completeSale(sale: Sale, items: List<SaleItem>) {
        val saleId = insertSale(sale)
        val itemsWithSaleId = items.map { it.copy(saleId = saleId) }
        insertSaleItems(itemsWithSaleId)
    }
}
