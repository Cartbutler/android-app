package com.example.cartbutler.network.networkModels

data class CartItem(
    val productId: Int,
    val quantity: Int,
    val products: Product
)