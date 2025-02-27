package com.example.cartbutler.network

import com.example.cartbutler.network.networkModels.ProductStore
import com.example.cartbutler.network.networkModels.Store

data class Product(
    val productId: Int,
    val productName: String,
    val description: String,
    val price: Float,
    val stock: Int,
    val categoryId: Int,
    val imagePath: String,
    val createdAt: String,
    val categoryName: String? = null,
    val productStore: List<ProductStore>? = null,
    val minPrice: Float? = null,
    val maxPrice: Float? = null,
    val stores: List<Store>? = null
)
