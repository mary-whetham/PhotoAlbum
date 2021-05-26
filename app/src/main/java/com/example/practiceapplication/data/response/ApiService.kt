package com.example.practiceapplication.data.response

import com.example.practiceapplication.ui.models.Photo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {

    @GET("photos")
    fun getAllPhotos(): Call<List<Photo>>

    @GET("albums/{albumId}/photos")
    fun getAlbum(
        @Path("albumId") albumId: Int
    ): Call<List<Photo>>

    @GET("photos/{photoId}")
    fun getPhoto(
        @Path("photoId") photoId: Int
    ): Call<Photo>

    companion object {

        var apiService: ApiService? = null

        fun getInstance(): ApiService {

            if (apiService == null) {
                val client = OkHttpClient.Builder().build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

                apiService = retrofit.create(ApiService::class.java)
            }

            return apiService!!
        }

    }

}