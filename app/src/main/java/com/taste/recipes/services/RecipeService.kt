package com.taste.recipes.services

import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.data.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("tag/{country}")
    suspend fun findRecipeByCountry(@Path("country") name:String): Response<RecipeResponse>
}