package com.example.mysocialmediaapp.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://meme-api.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun BuildMemeApi(): MemeApi {
        return retrofit.create(MemeApi::class.java)
    }






}