package com.example.beritakini.api

import com.example.beritakini.api.services.NewsServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://newsapi.org/v2/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val newsServices: NewsServices by lazy {
        retrofit.create(NewsServices::class.java)
    }
}