package com.example.practiceapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practiceapplication.data.repository.PhotoRepository
import com.example.practiceapplication.ui.models.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoViewModel constructor(private val repository: PhotoRepository): ViewModel() {

    val photo = MutableLiveData<Photo>()
    val photoList = MutableLiveData<List<Photo>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllPhotos() {

        val response = repository.getAllPhotos()
        response.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                photoList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })

    }

    fun getAlbum(albumId: Int) {

        val response = repository.getAlbum(albumId)
        response.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                photoList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })

    }

    fun getPhoto(photoId: Int) {

        val response = repository.getPhoto(photoId)
        response.enqueue(object : Callback<Photo> {
            override fun onResponse(call: Call<Photo>, response: Response<Photo>) {
                photo.postValue(response.body())
            }

            override fun onFailure(call: Call<Photo>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })

    }

}