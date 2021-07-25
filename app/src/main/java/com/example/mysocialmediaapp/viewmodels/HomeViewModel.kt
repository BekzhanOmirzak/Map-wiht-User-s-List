package com.example.mysocialmediaapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysocialmediaapp.models.Meme
import com.example.mysocialmediaapp.remote.MemeApi
import com.example.mysocialmediaapp.remote.SearchResponse
import com.example.mysocialmediaapp.remote.ServiceGenerator
import retrofit2.Call
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {



    private val TAG = "HomeViewModel"
    private val liveDataMemes = MutableLiveData<List<Meme>>()
    private val memeApi: MemeApi = ServiceGenerator.BuildMemeApi()

    init {
        val call = memeApi.getRandomMemes()
        call.enqueue(
            object : retrofit2.Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        val memes = response.body()?.memes
                        liveDataMemes.postValue(memes!!)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }

            })
    }

    fun getLiveDateMemes(): LiveData<List<Meme>> {
        return liveDataMemes
    }


}