package com.example.mysocialmediaapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.mysocialmediaapp.models.Meme
import com.example.mysocialmediaapp.remote.MemeApi
import com.example.mysocialmediaapp.remote.ServiceGenerator

object Repository {

    val memeApi: MemeApi = ServiceGenerator.BuildMemeApi()




}