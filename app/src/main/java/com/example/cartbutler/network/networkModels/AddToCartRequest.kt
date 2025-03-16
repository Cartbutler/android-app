package com.example.cartbutler.network.networkModels

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("productId") val productId: Int,
    val quantity: Int
)