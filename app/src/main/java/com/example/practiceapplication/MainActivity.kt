package com.example.practiceapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.data.repository.PhotoRepository
import com.example.practiceapplication.data.response.ApiService
import com.example.practiceapplication.ui.adapters.PhotoRecyclerAdapter
import com.example.practiceapplication.ui.viewmodels.PhotoViewModel
import com.example.practiceapplication.ui.viewmodels.PhotoViewModelFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var viewManager = LinearLayoutManager(this)
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var mainRecycler: RecyclerView

    private lateinit var editText: EditText

    private val apiService = ApiService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.num_photos)
        ArrayAdapter.createFromResource(this, R.array.photos_type_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        spinner.onItemSelectedListener = this

        editText = findViewById(R.id.photo_album_number)

        val button: Button = findViewById(R.id.photo_button)
        button.setOnClickListener {
            if (spinner.selectedItem.toString() == "A Photo") {
                photoViewModel.getPhoto(editText.text.toString().toInt())
            } else if (spinner.selectedItem.toString() == "An Album") {
                photoViewModel.getAlbum(editText.text.toString().toInt())
            } else if (spinner.selectedItem.toString() == "All Photos") {
                photoViewModel.getAllPhotos()
            }
            editText.text.clear()
        }

        mainRecycler = findViewById(R.id.recycler)
        val application = requireNotNull(this).application
        photoViewModel = ViewModelProvider(this, PhotoViewModelFactory(PhotoRepository(apiService))).get(
            PhotoViewModel::class.java)
        mainRecycler.layoutManager = viewManager
        val adapter = PhotoRecyclerAdapter(photoViewModel, this)
        mainRecycler.adapter = adapter

        photoViewModel.photo.observe(this, Observer {
            Log.i("data", it.toString())
            adapter.setPhotoList(mutableListOf(it))
        })

        photoViewModel.photoList.observe(this, Observer {
            Log.i("data", it.toString())
            adapter.setPhotoList(it)
        })

        photoViewModel.errorMessage.observe(this, Observer {
            Log.i("error", it.toString())
        })

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            if (parent.getItemAtPosition(position) == "A Photo") {
                editText.visibility = View.VISIBLE
                editText.setHint(R.string.photo_number)
            } else if (parent.getItemAtPosition(position) == "An Album") {
                editText.visibility = View.VISIBLE
                editText.setHint(R.string.album_number)
            } else if (parent.getItemAtPosition(position) == "All Photos") {
                editText.visibility = View.GONE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
