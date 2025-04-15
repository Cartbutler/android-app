package com.example.cartbutler.network.networkModels

data class Store(
    val storeId: Int? = null,
    val storeName: String,
    val storeLocation: String,
    val storeAddress: String?,
    val price: String? = null,
    val stock: Int? = null,
    val storeImage: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)