package com.example.cartbutler.repositories

import android.util.Log
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.AddToCartRequest
import retrofit2.HttpException
import java.io.IOException

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun addToCart(productId: Int, quantity: Int): Cart {
        try {
            val sessionId = sessionManager.getSessionId().also {
                require(!it.isNullOrEmpty()) { "Session ID cannot be null" }
            }

            val request = AddToCartRequest(
                userId = sessionId,
                productId = productId,
                quantity = quantity
            )

            val cart = apiService.addToCart(request)
            return cart

        } catch (e: HttpException) {
            val errorMsg = "HTTP error ${e.code()} - ${e.response()?.errorBody()?.string()}"
            Log.e("CartRepository", errorMsg, e)
            throw Exception("Error: ${e.message}")

        } catch (e: IOException) {
            Log.e("CartRepository", "Network error: ${e.message}", e)
            throw Exception("Check your connection")

        } catch (e: IllegalArgumentException) {
            Log.e("CartRepository", "Invalid data: ${e.message}", e)
            throw Exception("Error: ${e.message}")

        } catch (e: Exception) {
            Log.e("CartRepository", "Unexpected error: ${e.javaClass.simpleName}", e)
            throw Exception("Error: ${e.message}")
        }
    }

    suspend fun getCart(): Cart {
        try {
            val userId = sessionManager.getSessionId().also {
                Log.d("CartRepository", "SessionID: $it")
            }

            return apiService.getCart(userId)

        } catch (e: HttpException) {
            val errorMsg = "HTTP error ${e.code()} - ${e.response()?.errorBody()?.string()}"
            Log.e("CartRepository", errorMsg, e)
            throw Exception("Fail to search cart: ${e.message}")

        } catch (e: IOException) {
            Log.e("CartRepository", "Network error: ${e.message}", e)
            throw Exception("Check your network connection")

        } catch (e: Exception) {
            Log.e("CartRepository", "Unexpected error: ${e.javaClass.simpleName}", e)
            throw Exception("Error: ${e.message}")
        }
    }
}