package com.taste.recipes.services

import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.data.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {

    @GET("tag/{country}")
    suspend fun findRecipeByCountry(@Path("country") country:String): Response<RecipeResponse>

    @GET("{id}")
    suspend fun findRecipeById(@Path("id") id:String): Response<RecipeItemResponse>

    @GET("search")
    suspend fun findRecipeByName(@Query("q") q:String): Response<RecipeResponse>
}