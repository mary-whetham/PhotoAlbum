package com.example.practiceapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapplication.ui.models.Reminder
import java.time.LocalDate

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder WHERE user_id = :user_id")
    suspend fun getAllReminders(user_id: Int): List<Reminder>

    @Query("SELECT * FROM reminder WHERE user_id = :user_id AND date = :date")
    suspend fun getDaysReminders(user_id: Int, date: LocalDate): List<Reminder>

    @Insert
    suspend fun insert(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)
}