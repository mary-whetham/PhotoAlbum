package com.example.practiceapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practiceapplication.ui.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUser(id: Int): User

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun loginUser(email: String, password: String): User

    @Query("SELECT email FROM user WHERE email = :email")
    suspend fun getEmail(email: String): String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long
}