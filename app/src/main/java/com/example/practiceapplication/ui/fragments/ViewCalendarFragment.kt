package com.example.practiceapplication.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.practiceapplication.R
import com.example.practiceapplication.ui.adapters.EventRecyclerAdapter
import com.example.practiceapplication.ui.adapters.ReminderRecyclerAdapter
import com.example.practiceapplication.ui.viewmodels.CalendarViewModel
import kotlinx.android.synthetic.main.fragment_view_calendar.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class ViewCalendarFragment : Fragment() {

    private val calendarViewModel: CalendarViewModel by viewModels({requireParentFragment()})

    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var reminderAdapter: ReminderRecyclerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private var shownDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_calendar, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.getDaysEvents(shownDate)
        calendarViewModel.getDaysReminders(shownDate)

        eventAdapter = EventRecyclerAdapter(view.context)
        reminderAdapter = ReminderRecyclerAdapter(view.context)
        event_recycler.recyclerView.adapter = eventAdapter
        reminder_recycler.recyclerView.adapter = reminderAdapter

        checkIfEmpty()

        calendarViewModel.events.observe(viewLifecycleOwner, {
            eventAdapter.setEventList(it)
            checkIfEmpty()
        })

        calendarViewModel.reminders.observe(viewLifecycleOwner, {
            reminderAdapter.setReminderList(it)
            checkIfEmpty()
        })

        date.text = shownDate.format(formatter)

        left_button.setOnClickListener{
            shownDate = shownDate.minusDays(1)
            date.text = shownDate.format(formatter)
            calendarViewModel.getDaysEvents(shownDate)
            calendarViewModel.getDaysReminders(shownDate)
            checkIfEmpty()
        }

        right_button.setOnClickListener{
            shownDate = shownDate.plusDays(1)
            date.text = shownDate.format(formatter)
            calendarViewModel.getDaysEvents(shownDate)
            calendarViewModel.getDaysReminders(shownDate)
            checkIfEmpty()
        }

        add.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.calendar_container_view, AddCalendarFragment())
                setReorderingAllowed(true)
                addToBackStack("addToCalendar") // name can be null
            }
        }
    }

    private fun checkIfEmpty() {
        if (eventAdapter.itemCount == 0) {
            event_recycler.showEmptyMessage(getString(R.string.no_events))
        } else {
            event_recycler.updateViews(empty = false, success = true)
        }

        if (reminderAdapter.itemCount == 0) {
            reminder_recycler.showEmptyMessage(getString(R.string.no_reminders))
        } else {
            reminder_recycler.updateViews(empty = false, success = true)
        }
    }
}