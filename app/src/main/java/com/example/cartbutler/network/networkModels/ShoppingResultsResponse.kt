package com.example.cartbutler.network.networkModels

data class ShoppingResultsResponse(
    val storeId: Int,
    val storeName: String,
    val storeLocation: String,
    val storeAddress: String?,
    val latitude: Double?,
    val longitude: Double?,
    val storeImage: String?,
    val products: List<StoreProduct>,
    val total: Double
)