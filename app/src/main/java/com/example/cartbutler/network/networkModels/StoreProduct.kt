package com.example.cartbutler.network.networkModels

data class StoreProduct(
    val productName: String,
    val price: Double,
    val quantity: Int,
    val imagePath: String?
)