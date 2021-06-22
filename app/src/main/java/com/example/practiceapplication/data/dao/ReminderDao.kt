package com.example.practiceapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapplication.ui.models.Reminder
import java.time.LocalDate

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    suspend fun getAllReminders(): List<Reminder>

    @Query("SELECT * FROM reminder WHERE date = :date")
    suspend fun getDaysReminders(date: LocalDate): List<Reminder>

    @Insert
    suspend fun insert(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)
}