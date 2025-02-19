package com.example.cartbutler.network.networkModels

data class ProductStore(
    val productStoreId: Int,
    val productId: Int,
    val storeId: Int,
    val price: String,
    val stock: Int,
    val stores: Store
)