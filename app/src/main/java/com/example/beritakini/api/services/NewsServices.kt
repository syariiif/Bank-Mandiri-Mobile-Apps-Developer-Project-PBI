package com.example.beritakini.api.services

import com.example.beritakini.api.model.NewResponse
import retrofit2.Call
import retrofit2.http.GET

interface NewsServices {

    @GET("top-headlines?country=us&category=business&apiKey=8728b4c1f1994bca8c88a9c454de1b40")
    fun getAll(): Call<NewResponse>

}