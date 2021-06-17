package com.example.practiceapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapplication.R
import com.example.practiceapplication.ui.models.Reminder
import kotlinx.android.synthetic.main.reminder.view.*
import java.time.LocalDate
import java.util.*

class ReminderRecyclerAdapter(
    val context: Context,
    currentDate: LocalDate
) : RecyclerView.Adapter<ReminderRecyclerAdapter.ReminderViewHolder>() {

    private var reminders = mutableListOf<Reminder>()
    private var filteredReminders = mutableListOf<Reminder>()
    private var date = currentDate

    class ReminderViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        @ExperimentalStdlibApi
        fun bind(reminder: Reminder){
            ("Title: " + reminder.title).also { binding.reminder_title.text = it }
            ("Time: " + reminder.time).also { binding.reminder_time.text = it }
            ("Frequency of reminder: " +
                reminder.frequency.toString()
                    .replace("_", " ")
                    .lowercase()
                    .capitalize(Locale.ROOT))
                .also { binding.reminder_type.text = it }
        }
    }

    fun setDate(date: LocalDate) {
        this.date = date
        setFilteredReminderList()
    }

    fun setReminderList(reminders: List<Reminder>) {
        this.reminders = reminders.toMutableList()
        setFilteredReminderList()
    }

    private fun setFilteredReminderList() {
        this.filteredReminders = this.reminders.toMutableList().filter {
            it.date == this.date
        } as MutableList<Reminder>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReminderViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.reminder, parent, false)
        return ReminderViewHolder(root)
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(
        holder: ReminderViewHolder,
        position: Int
    ) {
        holder.bind(reminders[position])
    }

    override fun getItemCount(): Int {
        return filteredReminders.size
    }
}