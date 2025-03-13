package com.example.cartbutler.repositories

import android.util.Log
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.AddToCartRequest
import retrofit2.HttpException

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun addToCart(productId: Int, quantity: Int): Cart {
        try {
            Log.d(this::class.simpleName, "addToCart() - productId: $productId, quantity: $quantity")

            val sessionId = sessionManager.getSessionId()
            Log.d(this::class.simpleName, "Session ID: $sessionId")

            val request = AddToCartRequest(sessionId, productId, quantity)
            Log.d(this::class.simpleName, "Request: $request")

            val response = apiService.addToCart(request)
            Log.d(this::class.simpleName, "Response Code: ${response.code()}")
            Log.d(this::class.simpleName, "Raw JSON: ${response.body()?.toString()}")

            if (!response.isSuccessful) {
                Log.e(this::class.simpleName, "Error: ${response.errorBody()?.string()}")
                throw HttpException(response)
            }

            val cartResponse = response.body() ?: throw Exception("Empty cart response")
            Log.d(this::class.simpleName, "Cart Response: $cartResponse")

            return cartResponse

        } catch (e: Exception) {
            Log.e(this::class.simpleName, "addToCart failed: ${e.message}")
            throw Exception("Failed to add to cart: ${e.message}")
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