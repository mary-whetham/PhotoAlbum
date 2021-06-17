package com.example.practiceapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.R
import com.example.practiceapplication.data.repository.PhotoRepository
import com.example.practiceapplication.data.response.ApiService
import com.example.practiceapplication.ui.adapters.PhotoRecyclerAdapter
import com.example.practiceapplication.ui.viewmodels.PhotoViewModel
import com.example.practiceapplication.ui.viewmodels.PhotoViewModelFactory


class PhotoFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewManager: GridLayoutManager
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var mainRecycler: RecyclerView
    private lateinit var adapter: PhotoRecyclerAdapter

    private lateinit var editText: EditText

    private val apiService = ApiService.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerLayout: Spinner = view.findViewById(R.id.layout_type)
        ArrayAdapter.createFromResource(view.context, R.array.layout_type_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerLayout.adapter = adapter
            }
        spinnerLayout.onItemSelectedListener = this

        val spinnerPhoto: Spinner = view.findViewById(R.id.num_photos)
        ArrayAdapter.createFromResource(view.context, R.array.photos_type_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPhoto.adapter = adapter
            }
        spinnerPhoto.onItemSelectedListener = this

        editText = view.findViewById(R.id.photo_album_number)

        val button: Button = view.findViewById(R.id.photo_button)
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

        mainRecycler = view.findViewById(R.id.recycler)

        photoViewModel = ViewModelProvider(this, PhotoViewModelFactory(PhotoRepository(apiService))).get(
            PhotoViewModel::class.java)

        viewManager = GridLayoutManager(view.context, 1)
        mainRecycler.layoutManager = viewManager

        adapter = PhotoRecyclerAdapter(view.context)
        mainRecycler.adapter = adapter

        photoViewModel.photo.observe(viewLifecycleOwner, {
            adapter.setPhotoList(mutableListOf(it))
        })

        photoViewModel.photoList.observe(viewLifecycleOwner, {
            adapter.setPhotoList(it)
        })

        photoViewModel.errorMessage.observe(viewLifecycleOwner, {
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
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}