package com.example.practiceapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.R
import com.example.practiceapplication.ui.models.Photo
import com.example.practiceapplication.utils.GlideApp
import kotlinx.android.synthetic.main.photo.view.*

class PhotoRecyclerAdapter(
    val context: Context
) : RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder>() {

    private var photos = mutableListOf<Photo>()

    class PhotoViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun bind(photo: Photo){
            binding.photo_title.text = photo.title
            GlideApp.with(binding.context).load(photo.url).into(binding.photo_image)
        }
    }

    fun setPhotoList(photos: List<Photo>) {
        this.photos = photos.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return PhotoViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: PhotoViewHolder,
        position: Int
    ) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int {
        /*
        if (photos.isEmpty()) {
            Toast.makeText(context, "No photos", Toast.LENGTH_LONG).show()
        }*/

        return photos.size
    }
}