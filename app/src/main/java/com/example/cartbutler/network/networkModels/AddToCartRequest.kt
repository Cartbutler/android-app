package com.example.cartbutler.network.networkModels

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("product_id") val productId: Int,
    val quantity: Int
)