package com.example.mysocialmediaapp.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MemeApi {


    @GET("gimme/50")
     fun getRandomMemes(): Call<SearchResponse>


}