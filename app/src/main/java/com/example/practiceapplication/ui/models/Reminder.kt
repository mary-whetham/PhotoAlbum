package com.example.practiceapplication.ui.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "reminder",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Reminder(
    @ColumnInfo(name = "user_id")
    var user_id: Int,
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