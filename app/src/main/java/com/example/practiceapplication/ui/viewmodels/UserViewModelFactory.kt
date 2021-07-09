package com.example.practiceapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practiceapplication.data.repository.CalendarRepository

class UserViewModelFactory(
    private val calendarRepository: CalendarRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(calendarRepository) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }

}