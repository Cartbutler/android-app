package com.example.cartbutler.network

import com.example.cartbutler.network.networkModels.Category
import com.example.cartbutler.network.networkModels.Product
import com.example.cartbutler.network.networkModels.Cart
import com.example.cartbutler.network.networkModels.AddToCartRequest
import com.example.cartbutler.network.networkModels.ProductSuggestion
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.Body

interface ApiService {

    // Return categories list
    @GET("categories")
    suspend fun getCategories(): List<Category>

    // Return product suggestions
    @GET("suggestions")
    suspend fun getSuggestions(
        @Query("query") query: String
    ): List<ProductSuggestion>

    // Search for products by name/category
    @GET("search")
    suspend fun searchProducts(
        @Query("query") query: String?,
        @Query("categoryID") categoryID: Int? = null
    ): List<Product>

    // Return single product
    @GET("product")
    suspend fun getProductById(@Query("id") productId: Int): Product

    // Return cart for user
    @GET("cart")
    suspend fun getCart(
        @Query("userId") userId: String
    ): Cart

    // Add product to cart
    @POST("cart")
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ): Response<Cart>
}