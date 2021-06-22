package com.example.practiceapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.practiceapplication.data.dao.EventDao
import com.example.practiceapplication.data.dao.ReminderDao
import com.example.practiceapplication.ui.models.Event
import com.example.practiceapplication.ui.models.Reminder

@Database(
    entities = [Event::class, Reminder::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CalendarDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: CalendarDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CalendarDatabase::class.java, "todo-list.db"
            ).build()
    }
}