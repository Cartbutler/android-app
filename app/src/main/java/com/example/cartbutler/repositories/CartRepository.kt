package com.example.cartbutler.repositories

import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.networkModels.CartResponse

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun getCart(): CartResponse {
        return apiService.getCart(sessionManager.getSessionId())
    }
}