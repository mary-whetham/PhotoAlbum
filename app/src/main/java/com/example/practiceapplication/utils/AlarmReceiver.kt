package com.example.practiceapplication.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practiceapplication.MainActivity
import com.example.practiceapplication.R
import com.example.practiceapplication.ui.fragments.CalendarFragment
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.d("Alarm Bell", "Alarm just fired" + intent.extras?.getString("title"))

        val channelId = "1000"
        val notificationId: Long = Date().time


        val returnIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, returnIntent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_reservation)
            .setContentTitle(intent.extras?.getString("title"))
            .setContentText(intent.extras?.getString("description"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(100, 200))

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (notificationId != null) {
                notify(notificationId.toInt(), builder.build())
            }
        }
    }
}