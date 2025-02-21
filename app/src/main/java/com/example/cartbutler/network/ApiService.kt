package com.example.cartbutler.network

import com.example.cartbutler.network.networkModels.Category
import retrofit2.http.GET
import retrofit2.http.Query

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
}