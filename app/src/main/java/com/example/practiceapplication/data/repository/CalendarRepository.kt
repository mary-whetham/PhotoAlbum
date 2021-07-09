package com.example.practiceapplication.data.repository

import com.example.practiceapplication.data.CalendarDatabase
import com.example.practiceapplication.data.response.ApiService
import com.example.practiceapplication.ui.models.Event
import com.example.practiceapplication.ui.models.Reminder
import com.example.practiceapplication.ui.models.User
import java.time.LocalDate

class CalendarRepository constructor(
    private val calendarDatabase: CalendarDatabase
) {
    suspend fun getAllEvents(user_id: Int) = calendarDatabase.eventDao().getAllEvents(user_id)
    suspend fun getDaysEvents(user_id: Int, date: LocalDate) = calendarDatabase.eventDao().getDaysEvents(user_id, date)
    suspend fun insertEvent(event: Event) = calendarDatabase.eventDao().insert(event)
    suspend fun deleteAll() = calendarDatabase.eventDao().deleteAll()

    suspend fun getAllReminders(user_id: Int) = calendarDatabase.reminderDao().getAllReminders(user_id)
    suspend fun getDaysReminders(user_id: Int, date: LocalDate) = calendarDatabase.reminderDao().getDaysReminders(user_id, date)
    suspend fun insertReminder(reminder: Reminder) = calendarDatabase.reminderDao().insert(reminder)

    suspend fun getUser(id: Int) = calendarDatabase.userDao().getUser(id)
    suspend fun loginUser(email: String, password: String) = calendarDatabase.userDao().loginUser(email, password)
    suspend fun insertUser(user: User) = calendarDatabase.userDao().insert(user)
}