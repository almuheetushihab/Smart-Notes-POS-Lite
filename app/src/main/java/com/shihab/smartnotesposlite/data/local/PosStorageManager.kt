package com.shihab.smartnotesposlite.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shihab.smartnotesposlite.data.models.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "smart_pos_data")

class PosStorageManager(private val context: Context) {

    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, Product::class.java)
    private val adapter = moshi.adapter<List<Product>>(listType)

    private val PRODUCTS_KEY = stringPreferencesKey("saved_products_key")

    suspend fun saveProducts(productList: List<Product>) {
        val jsonText = adapter.toJson(productList)
        context.dataStore.edit { preferences ->
            preferences[PRODUCTS_KEY] = jsonText
        }
    }

    val getSavedProducts: Flow<List<Product>> = context.dataStore.data.map { preferences ->
        val savedJsonText = preferences[PRODUCTS_KEY] ?: ""
        if (savedJsonText.isNotEmpty()) {
            adapter.fromJson(savedJsonText) ?: emptyList()
        } else {
            emptyList()
        }
    }
}