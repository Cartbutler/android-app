package com.example.cartbutler.repositories

import android.util.Log
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.AddToCartRequest

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    // CartRepository.kt
    suspend fun addToCart(productId: Int, quantity: Int): Cart {
        try {
            val request = AddToCartRequest(
                userId = sessionManager.getSessionId(),
                productId = productId,
                quantity = quantity
            )

            val cart = apiService.addToCart(request)

            require(cart.userId.isNotEmpty()) { "User ID cannot be null" }
            require(cart.cartItems.isNotEmpty()) { "Cart cannot be empty" }

            return cart

        } catch (e: Exception) {
            throw Exception("Cannot add to cart: ${e.message}")
        }
    }

    suspend fun getCart(): Cart {
        try {
            Log.d(this::class.simpleName, "getCart() called")

            val userId = sessionManager.getSessionId()
            Log.d(this::class.simpleName, "Session ID: $userId")

            val cart = apiService.getCart(userId)
            Log.d(this::class.simpleName, "Cart Items: ${cart.cartItems.size}")
            Log.d(this::class.simpleName, "Cart Quantities: ${cart.cartItems.map { it.quantity }}")

            return cart
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "getCart failed: ${e.message}")
            throw Exception("Failed to fetch cart: ${e.message}")
        }
    }
}