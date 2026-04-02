package com.shihab.smartnotesposlite.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    val id: Long,
    val name: String,
    val price: Double
)