package com.example.practiceapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CalendarViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CalendarViewModel::class.java)){
            return CalendarViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }

}