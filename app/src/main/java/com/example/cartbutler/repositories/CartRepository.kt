package com.example.cartbutler.repositories

import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.networkModels.CartResponse
import retrofit2.HttpException

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun getCart(): CartResponse {
        return apiService.getCart(sessionManager.getSessionId())
    }

    suspend fun addToCart(productId: Int, quantity: Int) {
        try {
            val response = apiService.addToCart(
                userId = sessionManager.getSessionId(),
                productId = productId,
                quantity = quantity
            )

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            throw Exception("Failed to add to cart: ${e.message}")
        }
    }
}