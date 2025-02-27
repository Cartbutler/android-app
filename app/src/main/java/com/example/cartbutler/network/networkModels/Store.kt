package com.example.cartbutler.network.networkModels

data class Store(
    val storeId: Int? = null,
    val storeName: String,
    val storeLocation: String,
    val price: String? = null,
    val stock: Int? = null
)