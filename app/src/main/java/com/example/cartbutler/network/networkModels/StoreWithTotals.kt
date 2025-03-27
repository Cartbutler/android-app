package com.example.cartbutler.network.networkModels

data class StoreWithTotals(
    val store: Store,
    val totalItems: Int,
    val totalPrice: Float
)