package com.example.practiceapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.data.repository.PhotoRepository
import com.example.practiceapplication.data.response.ApiService
import com.example.practiceapplication.ui.adapters.PhotoRecyclerAdapter
import com.example.practiceapplication.ui.viewmodels.PhotoViewModel
import com.example.practiceapplication.ui.viewmodels.PhotoViewModelFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var viewManager = GridLayoutManager(this, 1)
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var mainRecycler: RecyclerView
    private lateinit var adapter: PhotoRecyclerAdapter

    private lateinit var editText: EditText

    private val apiService = ApiService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerLayout: Spinner = findViewById(R.id.layout_type)
        ArrayAdapter.createFromResource(this, R.array.layout_type_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerLayout.adapter = adapter
            }
        spinnerLayout.onItemSelectedListener = this

        val spinnerPhoto: Spinner = findViewById(R.id.num_photos)
        ArrayAdapter.createFromResource(this, R.array.photos_type_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPhoto.adapter = adapter
            }
        spinnerPhoto.onItemSelectedListener = this

        editText = findViewById(R.id.photo_album_number)

        val button: Button = findViewById(R.id.photo_button)
        button.setOnClickListener {
            if (spinnerPhoto.selectedItem.toString() == "A Photo") {
                photoViewModel.getPhoto(editText.text.toString().toInt())
            } else if (spinnerPhoto.selectedItem.toString() == "An Album") {
                photoViewModel.getAlbum(editText.text.toString().toInt())
            } else if (spinnerPhoto.selectedItem.toString() == "All Photos") {
                photoViewModel.getAllPhotos()
            }
            editText.text.clear()
        }

        mainRecycler = findViewById(R.id.recycler)
        val application = requireNotNull(this).application

        photoViewModel = ViewModelProvider(this, PhotoViewModelFactory(PhotoRepository(apiService))).get(
            PhotoViewModel::class.java)

        mainRecycler.layoutManager = viewManager

        adapter = PhotoRecyclerAdapter(photoViewModel, this)
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
        if (parent != null && parent.id == R.id.num_photos) {
            if (parent.getItemAtPosition(position) == "A Photo") {
                editText.visibility = View.VISIBLE
                editText.setHint(R.string.photo_number)
            } else if (parent.getItemAtPosition(position) == "An Album") {
                editText.visibility = View.VISIBLE
                editText.setHint(R.string.album_number)
            } else if (parent.getItemAtPosition(position) == "All Photos") {
                editText.visibility = View.GONE
            }
        } else if (parent != null && parent.id == R.id.layout_type) {
            if (parent.getItemAtPosition(position) == "One Column") {
                viewManager.spanCount = 1
            } else if (parent.getItemAtPosition(position) == "Two Columns") {
                viewManager.spanCount = 2
            }
            adapter.notifyItemRangeChanged(0, adapter.itemCount ?: 0)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
