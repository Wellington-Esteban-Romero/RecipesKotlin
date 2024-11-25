package com.superheroes.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

   /* companion object {

        private val BASE_URL = "https://superheroapi.com/api/9afdf0a4d31b4337bfe784d5d63722bc/"

        fun getRetrofit(): SuperheroService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(SuperheroService::class.java)
        }
    }*/
}