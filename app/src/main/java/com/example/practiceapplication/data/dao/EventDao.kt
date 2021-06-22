package com.example.practiceapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapplication.ui.models.Event
import java.time.LocalDate

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM event WHERE date = :date")
    suspend fun getDaysEvents(date: LocalDate): List<Event>

    @Insert
    suspend fun insert(event: Event)

    @Query("DELETE FROM event")
    suspend fun deleteAll()
}