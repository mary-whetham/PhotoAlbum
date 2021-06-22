package com.example.practiceapplication.data.repository

import com.example.practiceapplication.data.CalendarDatabase
import com.example.practiceapplication.data.response.ApiService
import com.example.practiceapplication.ui.models.Event
import com.example.practiceapplication.ui.models.Reminder
import java.time.LocalDate

class CalendarRepository constructor(
    private val calendarDatabase: CalendarDatabase
) {
    suspend fun getAllEvents() = calendarDatabase.eventDao().getAllEvents()
    suspend fun getDaysEvents(date: LocalDate) = calendarDatabase.eventDao().getDaysEvents(date)
    suspend fun insertEvent(event: Event) = calendarDatabase.eventDao().insert(event)
    suspend fun deleteAll() = calendarDatabase.eventDao().deleteAll()

    suspend fun getAllReminders() = calendarDatabase.reminderDao().getAllReminders()
    suspend fun getDaysReminders(date: LocalDate) = calendarDatabase.reminderDao().getDaysReminders(date)
    suspend fun insertReminder(reminder: Reminder) = calendarDatabase.reminderDao().insert(reminder)
}