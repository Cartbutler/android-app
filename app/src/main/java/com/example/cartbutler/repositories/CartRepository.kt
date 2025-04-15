package com.example.cartbutler.repositories

import android.content.Context
import android.util.Log
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.LocaleHelper
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.AddToCartRequest
import com.example.cartbutler.network.networkModels.ShoppingResultsResponse
import retrofit2.HttpException
import java.io.IOException

class CartRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val context: Context
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

            return apiService.addToCart(request)

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

            return apiService.getCart(userId, languageId = LocaleHelper.currentLanguage(context))

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

    suspend fun getShoppingResults(cartId: Int): List<ShoppingResultsResponse> {
        if (cartId == 0) return emptyList()

        try {
            val userId = sessionManager.getSessionId()
            Log.d("CartRepository", "CartID: $cartId, UserID: $userId")
            return apiService.getShoppingResults(cartId, userId,
                languageId = LocaleHelper.currentLanguage(context))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
            Log.e("CartRepository", """
             HTTP Error ${e.code()}
             URL: ${e.response()?.raw()?.request?.url}
             Body: $errorBody
        """.trimIndent())

            throw Exception("Fail to search cart: ${e.message}")

        } catch (e: IOException) {
            Log.e("CartRepository", "Network error: ${e.message}", e)
            throw Exception("Check your connection")

        } catch (e: Exception) {
            Log.e("CartRepository", "Error: ${e.message}", e)
            throw e
        }
    }
}