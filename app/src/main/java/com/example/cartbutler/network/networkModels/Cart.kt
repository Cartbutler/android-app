package com.example.cartbutler.network.networkModels

data class Cart(
    val id: Int,
    val cartItems: List<CartItem> = emptyList()
)