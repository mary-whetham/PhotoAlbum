package com.example.practiceapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.R
import com.example.practiceapplication.ui.models.Event
import kotlinx.android.synthetic.main.event.view.*
import java.time.LocalDate
import java.util.*

class EventRecyclerAdapter(
    val context: Context,
    currentDate: LocalDate
) : RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder>() {

    private var events = mutableListOf<Event>()
    private var filteredEvents = mutableListOf<Event>()
    private var date = currentDate

    class EventViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        @ExperimentalStdlibApi
        fun bind(event: Event){
            ("Title: " + event.title).also { binding.event_title.text = it }
            ("Description: " + event.description).also { binding.event_description.text = it }
            ("Location: " + event.location).also { binding.event_location.text = it }
            ("Time: " + event.time).also { binding.event_time.text = it }
            ("Frequency of event: " + event.frequency.toString().lowercase().capitalize(Locale.ROOT)).also { binding.event_type.text = it }
        }
    }

    fun setDate(date: LocalDate) {
        this.date = date
        setFilteredEventList()
    }

    fun setEventList(events: List<Event>) {
        this.events = events.toMutableList()
        setFilteredEventList()
    }

    private fun setFilteredEventList() {
        this.filteredEvents = this.events.toMutableList().filter {
            it.date == this.date
        } as MutableList<Event>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.event, parent, false)
        return EventViewHolder(root)
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        holder.bind(filteredEvents[position])
    }

    override fun getItemCount(): Int {
        return filteredEvents.size
    }
}