package com.example.cartbutler.network

data class Product(
    val product_id: Int,
    val product_name: String,
    val description: String,
    val price: Float,
    val stock: Int,
    val category_id: Int,
    val image_path: String,
    val created_at: String
)