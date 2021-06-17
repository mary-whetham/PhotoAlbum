package com.example.practiceapplication.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.databinding.RecyclerviewEmptySupportBinding

class RecyclerViewEmptySupport(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val binding: RecyclerviewEmptySupportBinding =
        RecyclerviewEmptySupportBinding.inflate(LayoutInflater.from(context), this, true)
    val recyclerView: RecyclerView = binding.recyclerView

    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.isNestedScrollingEnabled = false
    }

    fun updateViews(
        empty: Boolean = true,
        success: Boolean = false
    ) {
        binding.recyclerView.visibility = if (success) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.emptyTextView.visibility = if (empty) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun showEmptyMessage(message: String) {
        updateViews(empty = true)
        binding.emptyTextView.text = message
    }
}