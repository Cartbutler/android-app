package com.example.cartbutler.network.networkModels

import com.google.gson.annotations.SerializedName

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

data class AddToCartRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("productId") val productId: Int,
    val quantity: Int
)