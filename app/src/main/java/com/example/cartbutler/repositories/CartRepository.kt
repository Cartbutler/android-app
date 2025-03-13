package com.example.cartbutler.repositories

import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.networkModels.CartResponse
import com.example.cartbutler.network.networkModels.AddToCartRequest
import retrofit2.HttpException

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun addToCart(productId: Int, quantity: Int) {
        try {
            val request = AddToCartRequest(
                userId = sessionManager.getSessionId(),
                productId = productId,
                quantity = quantity
            )

            val response = apiService.addToCart(request)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            throw Exception("Failed to add to cart: ${e.message}")
        }
    }

    suspend fun getCart(): CartResponse {
        try {
            val userId = sessionManager.getSessionId()
            return apiService.getCart(userId)
        } catch (e: Exception) {
            throw Exception("Failed to fetch cart: ${e.message}")
        }
    }
}