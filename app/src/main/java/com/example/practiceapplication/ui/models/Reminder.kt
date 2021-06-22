package com.example.practiceapplication.ui.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "reminder")
data class Reminder(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "date")
    var date: LocalDate,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "type")
    var type: Boolean,
    @ColumnInfo(name = "frequency")
    var frequency: REMINDER_FREQUENCY
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}