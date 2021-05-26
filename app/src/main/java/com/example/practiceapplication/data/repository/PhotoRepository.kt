package com.example.practiceapplication.data.repository

import com.example.practiceapplication.data.response.ApiService

class PhotoRepository constructor(
    private val apiService: ApiService
) {
    fun getAllPhotos() = apiService.getAllPhotos()
    fun getAlbum(albumId: Int) = apiService.getAlbum(albumId)
    fun getPhoto(photoId: Int) = apiService.getPhoto(photoId)
}