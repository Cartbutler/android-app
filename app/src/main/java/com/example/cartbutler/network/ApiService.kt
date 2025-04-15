package com.example.cartbutler.network

import com.example.cartbutler.network.networkModels.Category
import com.example.cartbutler.network.networkModels.Product
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.AddToCartRequest
import com.example.cartbutler.network.networkModels.ProductSuggestion
import com.example.cartbutler.network.networkModels.ShoppingResultsResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface ApiService {

    // Return categories list
    @GET("categories")
    suspend fun getCategories(
        @Query("language_id") languageId: String
    ): List<Category>

    // Return product suggestions
    @GET("suggestions")
    suspend fun getSuggestions(
        @Query("query") query: String
    ): List<ProductSuggestion>

    // Search for products by name/category
    @GET("search")
    suspend fun searchProducts(
        @Query("query") query: String?,
        @Query("category_id") categoryID: Int? = null,
        @Query("language_id") languageId: String
    ): List<Product>

    // Return single product
    @GET("product")
    suspend fun getProductById(
        @Query("id") productId: Int,
        @Query("language_id") languageId: String
    ): Product

    // Return cart for user
    @GET("cart")
    suspend fun getCart(
        @Query("user_id") userId: String,
        @Query("language_id") languageId: String
    ): Cart

    // Add product to cart
    @POST("cart")
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ): Cart

    // Fetch shopping results
    @GET("shopping-results")
    suspend fun getShoppingResults(
        @Query("cart_id") cartId: Int,
        @Query("user_id") userId: String,
        @Query("language_id") languageId: String
    ): List<ShoppingResultsResponse>
}