package com.example.cartbutler.network.networkModels

data class Cart(
    val id: Int,
    val userId: String,
    val cartItems: List<CartItem>
)

data class CartItem(
    val productId: Int,
    val quantity: Int,
    val products: Product
)

data class CartResponse(
    val id: Int,
    val userId: String,
    val cartItems: List<CartItemResponse>
)

data class CartItemResponse(
    val productId: Int,
    val quantity: Int,
    val products: Product
)