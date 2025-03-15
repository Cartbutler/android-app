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
            Log.d("CartRepository", "Iniciando addToCart()")
            Log.d("CartRepository", "Parâmetros - ProductID: $productId, Quantity: $quantity")

            val sessionId = sessionManager.getSessionId().also {
                Log.d("CartRepository", "SessionID: $it")
                require(!it.isNullOrEmpty()) { "Session ID não pode ser nulo!" }
            }

            val request = AddToCartRequest(
                userId = sessionId,
                productId = productId,
                quantity = quantity
            ).also {
                Log.d("CartRepository", "Request: ${it.userId}, ${it.productId}, ${it.quantity}")
            }


            Log.d("CartRepository", "Enviando requisição para API...")
            val cart = apiService.addToCart(request).also {
                Log.d("CartRepository", "Resposta recebida: ${it.cartItems.size} itens")
                Log.d("CartRepository", "Detalhes do carrinho: $it")
            }

            Log.d("CartRepository", "Validando campos obrigatórios...")
            require(cart.cartItems.isNotEmpty()) {
                "Carrinho vazio na resposta. Resposta completa: $cart"
            }

            Log.d("CartRepository", "AddToCart() concluído com sucesso!")
            return cart

        } catch (e: HttpException) {
            val errorMsg = "Erro HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
            Log.e("CartRepository", errorMsg, e)
            throw Exception("Falha na comunicação com o servidor: ${e.message}")

        } catch (e: IOException) {
            Log.e("CartRepository", "Erro de rede: ${e.message}", e)
            throw Exception("Verifique sua conexão com a internet")

        } catch (e: IllegalArgumentException) {
            Log.e("CartRepository", "Dados inválidos: ${e.message}", e)
            throw Exception("Resposta inválida do servidor: ${e.message}")

        } catch (e: Exception) {
            Log.e("CartRepository", "Erro inesperado: ${e.javaClass.simpleName}", e)
            throw Exception("Erro ao adicionar ao carrinho: ${e.message}")
        }
    }

    suspend fun getCart(): Cart {
        try {
            Log.d("CartRepository", "Iniciando getCart()")

            val userId = sessionManager.getSessionId().also {
                Log.d("CartRepository", "SessionID: $it")
            }

            Log.d("CartRepository", "Buscando carrinho na API...")
            return apiService.getCart(userId).also {
                Log.d("CartRepository", "Carrinho obtido: ${it.cartItems.size} itens")
                Log.d("CartRepository", "Itens detalhados: ${it.cartItems.joinToString {
                    "ID ${it.productId} (Qtd: ${it.quantity})"
                }}")
            }

        } catch (e: HttpException) {
            val errorMsg = "Erro HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
            Log.e("CartRepository", errorMsg, e)
            throw Exception("Falha ao buscar carrinho: ${e.message}")

        } catch (e: IOException) {
            Log.e("CartRepository", "Erro de rede: ${e.message}", e)
            throw Exception("Verifique sua conexão com a internet")

        } catch (e: Exception) {
            Log.e("CartRepository", "Erro inesperado: ${e.javaClass.simpleName}", e)
            throw Exception("Erro ao carregar carrinho: ${e.message}")
        }
    }
}