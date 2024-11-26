package com.taste.recipes.utils

import com.taste.recipes.services.RecipeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

    companion object {

        private const val BASE_URL = "https://dummyjson.com/recipes/"

        fun getRetrofit(): RecipeService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RecipeService::class.java)
        }
    }
}