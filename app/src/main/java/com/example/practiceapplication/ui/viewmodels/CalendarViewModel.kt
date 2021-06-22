package com.example.practiceapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapplication.data.repository.CalendarRepository
import com.example.practiceapplication.ui.models.Event
import com.example.practiceapplication.ui.models.Reminder
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
): ViewModel() {

    var events = MutableLiveData<List<Event>>()
    var reminders = MutableLiveData<List<Reminder>>()

    fun getAllEvents() {
        viewModelScope.launch {
            try {
                events.postValue(calendarRepository.getAllEvents())
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun getDaysEvents(date: LocalDate) {
        viewModelScope.launch {
            try {
                events.postValue(calendarRepository.getDaysEvents(date))
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                calendarRepository.insertEvent(event)
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun deleteAllEvents() {
        viewModelScope.launch {
            try {
                calendarRepository.deleteAll()
            } catch (e: Exception) {

            }
        }
    }

    fun getAllReminders() {
        viewModelScope.launch {
            try {
                reminders.postValue(calendarRepository.getAllReminders())
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun getDaysReminders(date: LocalDate) {
        viewModelScope.launch {
            try {
                reminders.postValue(calendarRepository.getDaysReminders(date))
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                calendarRepository.insertReminder(reminder)
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }
}