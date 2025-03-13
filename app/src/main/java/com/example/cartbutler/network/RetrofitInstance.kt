package com.example.cartbutler.network

import com.google.gson.GsonBuilder
import com.google.gson.FieldNamingPolicy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cartbutler.network.networkModels.AddToCartRequest

object RetrofitInstance {
    private const val BASE_URL = "https://southern-shard-449119-d4.nn.r.appspot.com/"

    val gson = GsonBuilder()
        .setFieldNamingStrategy { field ->
            if (field.declaringClass == AddToCartRequest::class.java &&
                (field.name == "userId" || field.name == "productId")
            ) {
                field.name
            } else {
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field)
            }
        }
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}