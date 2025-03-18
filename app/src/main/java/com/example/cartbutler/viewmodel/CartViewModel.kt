package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.repositories.CartRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
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

    private val updateEvents = Channel<Pair<Int, Int>>(Channel.CONFLATED)
    private val debounceInterval = 500L

    init {
        viewModelScope.launch {
            updateEvents.receiveAsFlow()
                .debounce(debounceInterval)
                .collect { (productId, quantity) ->
                    performQuantityUpdate(productId, quantity)
                }
        }
        loadInitialCart()
    }

    fun incrementQuantity(productId: Int) {
        val currentQuantity = _cart.value?.cartItems?.find { it.product.productId == productId }?.quantity ?: 0
        sendUpdateEvent(productId, currentQuantity + 1)
    }

    fun decrementQuantity(productId: Int) {
        val currentQuantity = _cart.value?.cartItems?.find { it.product.productId == productId }?.quantity ?: 0
        sendUpdateEvent(productId, (currentQuantity - 1).coerceAtLeast(0))
    }

    fun removeItem(productId: Int) {
        sendUpdateEvent(productId, 0)
    }

    private fun sendUpdateEvent(productId: Int, quantity: Int) {
        viewModelScope.launch {
            updateEvents.send(productId to quantity)
        }
    }

    private suspend fun performQuantityUpdate(productId: Int, newQuantity: Int) {
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

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
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