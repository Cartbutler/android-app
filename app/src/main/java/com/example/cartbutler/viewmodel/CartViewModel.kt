package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.repositories.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItemsCount = MutableStateFlow(0)
    val cartItemsCount: StateFlow<Int> = _cartItemsCount.asStateFlow()

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val debounceMutex = Mutex()
    private var lastClickTime = 0L
    private val debounceInterval = 500L

    init {
        loadInitialCart()
    }

    private fun loadInitialCart() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val cart = repository.getCart()
                _cart.value = cart
                _cartItemsCount.value = cart.cartItems.sumOf { it.quantity }
            } catch (e: Exception) {
                _error.value = "Error loading cart: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun refreshCart() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val cart = repository.getCart()
                _cart.value = cart
                _cartItemsCount.value = cart.cartItems.sumOf { it.quantity }
            } catch (e: Exception) {
                _error.value = "Error refreshing cart: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            debounceMutex.withLock {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < debounceInterval) return@withLock
                lastClickTime = currentTime

                _loading.value = true
                try {
                    repository.addToCart(productId, newQuantity)
                    refreshCart()
                } catch (e: Exception) {
                    _error.value = "Error updating quantity: ${e.message}"
                } finally {
                    _loading.value = false
                }
            }
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            debounceMutex.withLock {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < debounceInterval) return@withLock
                lastClickTime = currentTime

                _loading.value = true
                try {
                    val currentCart = repository.getCart()
                    val existingItem = currentCart.cartItems.find { it.product.productId == productId }
                    val newQuantity = existingItem?.quantity?.plus(1) ?: 1

                    repository.addToCart(productId, newQuantity)
                    refreshCart()
                } catch (e: Exception) {
                    _error.value = "Error adding to cart: ${e.message}"
                } finally {
                    _loading.value = false
                }
            }
        }
    }
}