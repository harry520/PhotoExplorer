package com.example.photoexplorer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {
    @GET("photos")
    fun getPhotos(@Query("client_id") apiKey: String): Call<List<UnsplashPhoto>>
}