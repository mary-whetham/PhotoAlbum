package com.example.practiceapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.practiceapplication.R
import com.example.practiceapplication.data.CalendarDatabase
import com.example.practiceapplication.data.repository.CalendarRepository
import com.example.practiceapplication.ui.viewmodels.CalendarViewModel
import com.example.practiceapplication.ui.viewmodels.CalendarViewModelFactory


class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarDatabase by lazy { CalendarDatabase.invoke(view.context) }
        val calendarRepository by lazy { CalendarRepository(calendarDatabase) }
        calendarViewModel = ViewModelProvider(this, CalendarViewModelFactory(calendarRepository)).get(CalendarViewModel::class.java)

        childFragmentManager.beginTransaction().replace(R.id.calendar_container_view, ViewCalendarFragment()).commit()
    }
}