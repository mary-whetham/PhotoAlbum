package com.example.practiceapplication.ui.models

import java.time.LocalDate

data class Event(
    var title: String,
    var description: String,
    var location: String,
    var date: LocalDate,
    var time: String,
    var type: Boolean,
    var frequency: EVENT_FREQUENCY
)