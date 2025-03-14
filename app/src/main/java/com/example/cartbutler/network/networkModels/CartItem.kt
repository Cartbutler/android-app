package com.example.cartbutler.network.networkModels

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("productId") val productId: Int,
    val quantity: Int,
    @SerializedName("products") val product: Product
)