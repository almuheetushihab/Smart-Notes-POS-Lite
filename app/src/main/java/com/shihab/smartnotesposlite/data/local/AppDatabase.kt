package com.shihab.smartnotesposlite.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shihab.smartnotesposlite.data.models.Product
import com.shihab.smartnotesposlite.data.models.Sale
import com.shihab.smartnotesposlite.data.models.SaleItem

@Database(entities = [Product::class, Sale::class, SaleItem::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_pos_database"
                )
                .fallbackToDestructiveMigration() // Simple for now, wipes data on version change
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
