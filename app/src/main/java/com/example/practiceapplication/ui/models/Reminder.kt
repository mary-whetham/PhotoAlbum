package com.example.practiceapplication.ui.models

import java.time.LocalDate

data class Reminder(
    var title: String,
    var date: LocalDate,
    var time: String,
    var type: Boolean,
    var frequency: REMINDER_FREQUENCY
)