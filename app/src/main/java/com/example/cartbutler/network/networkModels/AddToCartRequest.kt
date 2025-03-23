package com.example.cartbutler.network.networkModels

data class AddToCartRequest(
    val userId: String,
    val productId: Int,
    val quantity: Int
)