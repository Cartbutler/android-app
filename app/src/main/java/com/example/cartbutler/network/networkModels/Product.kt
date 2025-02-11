package com.example.cartbutler.network

data class Product(
    val productId: Int,
    val productName: String,
    val description: String,
    val price: Float,
    val stock: Int,
    val categoryId: Int,
    val imagePath: String,
    val createdAt: String
)