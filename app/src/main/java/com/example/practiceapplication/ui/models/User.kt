package com.example.practiceapplication.ui.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "firstName")
    var firstName: String,
    @ColumnInfo(name = "lastName")
    var lastName: String,
    @ColumnInfo(name = "password")
    var password: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
