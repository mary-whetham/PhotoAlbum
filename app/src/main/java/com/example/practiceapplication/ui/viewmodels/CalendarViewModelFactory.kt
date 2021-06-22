package com.example.practiceapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practiceapplication.data.repository.CalendarRepository

class CalendarViewModelFactory(
    private val calendarRepository: CalendarRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CalendarViewModel::class.java)){
            return CalendarViewModel(calendarRepository) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }

}