package com.example.cartbutler.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.ShoppingResultsResponse
import com.example.cartbutler.network.networkModels.Store
import com.example.cartbutler.network.networkModels.StoreWithTotals
import com.example.cartbutler.repositories.CartRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart.asStateFlow()

    private val _pendingDeltas = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val pendingDeltas: StateFlow<Map<Int, Int>> = _pendingDeltas.asStateFlow()

    private val _storeResults = MutableStateFlow<List<StoreWithTotals>>(emptyList())
    val storeResults: StateFlow<List<StoreWithTotals>> = _storeResults.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cartItemsCount = MutableStateFlow(0)
    val cartItemsCount: StateFlow<Int> = _cartItemsCount.asStateFlow()

    val _selectedStoreProducts = mutableStateOf<ShoppingResultsResponse?>(null)
    val selectedStoreProducts: State<ShoppingResultsResponse?> = _selectedStoreProducts

    val _shoppingResultsResponses = MutableStateFlow<List<ShoppingResultsResponse>>(emptyList())

    private val debounceJobs = mutableMapOf<Int, Job>()

    init {
        loadInitialCart()
    }

    fun incrementQuantity(productId: Int) {
        updatePendingDelta(productId, +1)
    }

    fun decrementQuantity(productId: Int) {
        updatePendingDelta(productId, -1)
    }

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            debounceJobs[productId]?.cancel()
            debounceJobs.remove(productId)
            _pendingDeltas.update { current ->
                current.toMutableMap().apply { remove(productId) }
            }
            performQuantityUpdate(productId, 0)
        }
    }

    private fun updatePendingDelta(productId: Int, delta: Int) {
        _pendingDeltas.update { current ->
            val newMap = current.toMutableMap()
            newMap[productId] = (newMap[productId] ?: 0) + delta
            newMap
        }
        scheduleDebounce(productId)
    }

    private fun scheduleDebounce(productId: Int) {
        debounceJobs[productId]?.cancel()
        val newJob = viewModelScope.launch {
            delay(500)
            val delta = _pendingDeltas.value[productId] ?: 0
            _pendingDeltas.update { current ->
                val newMap = current.toMutableMap()
                newMap.remove(productId)
                newMap
            }
            performDeltaUpdate(productId, delta)
            debounceJobs.remove(productId)
        }
        debounceJobs[productId] = newJob
    }

    private suspend fun performDeltaUpdate(productId: Int, delta: Int) {
        val currentQuantity = _cart.value?.cartItems?.find { it.products.productId == productId }?.quantity ?: 0
        val newQuantity = currentQuantity + delta
        if (newQuantity < 0) return

        _loading.value = true
        try {
            repository.addToCart(productId, newQuantity)
            refreshCart()
        } catch (e: Exception) {
            _error.value = "Error updating quantity: ${e.message}"
            _pendingDeltas.update { current ->
                val newMap = current.toMutableMap()
                newMap[productId] = (newMap[productId] ?: 0) + delta
                newMap
            }
        } finally {
            _loading.value = false
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
                _cartItemsCount.value = cart.cartItems.size
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
                Log.d("CartViewModel", "CartID: ${cart.id}, Items: ${cart.cartItems.size}")
                _cartItemsCount.value = cart.cartItems.size

                if (cart.cartItems.isNotEmpty()) {
                    loadShoppingResults(cart.id)
                } else {
                    _storeResults.value = emptyList()
                }
            } catch (e: Exception) {
                _error.value = "Error refreshing cart: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun loadShoppingResults(cartId: Int) {
        try {
            val results = repository.getShoppingResults(cartId)
            _shoppingResultsResponses.value = results
            _storeResults.value = results.map { response ->
                StoreWithTotals(
                    store = Store(
                        storeId = response.storeId,
                        storeName = response.storeName,
                        storeLocation = response.storeLocation,
                        price = response.total.toString(),
                        stock = null,
                        storeImage = response.storeImage
                    ),
                    totalItems = response.products.sumOf { it.quantity },
                    totalPrice = response.total.toFloat()
                )
            }
        } catch (e: Exception) {
            _error.value = "Error loading stores: ${e.message}"
        }
    }
}