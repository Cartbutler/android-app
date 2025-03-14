package com.example.cartbutler.network.networkModels

import com.google.gson.annotations.SerializedName

data class Cart(
    val id: Int,
    val userId: String,
    @SerializedName("cartItems") val cartItems: List<CartItem> = emptyList()
)