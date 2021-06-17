package com.example.practiceapplication.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practiceapplication.ui.models.Event
import com.example.practiceapplication.ui.models.Reminder

class CalendarViewModel: ViewModel() {

    var events = MutableLiveData<ArrayList<Event>>()
    private var thisEvent = arrayListOf<Event>()

    var reminders = MutableLiveData<ArrayList<Reminder>>()
    private var thisReminder = arrayListOf<Reminder>()

    fun addEvent(event: Event) {
        thisEvent.add(event)
        events.value = thisEvent
    }

    fun addReminder(reminder: Reminder) {
        thisReminder.add(reminder)
        reminders.value = thisReminder
    }
}