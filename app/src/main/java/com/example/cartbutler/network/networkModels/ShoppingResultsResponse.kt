package com.example.cartbutler.network.networkModels

data class ShoppingResultsResponse(
    val storeId: Int,
    val storeName: String,
    val storeLocation: String,
    val products: List<StoreProduct>,
    val total: Double
)