package com.example.practiceapplication.ui.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.practiceapplication.R
import com.example.practiceapplication.data.CalendarDatabase
import com.example.practiceapplication.data.repository.CalendarRepository
import com.example.practiceapplication.ui.adapters.TabAdapter
import com.example.practiceapplication.ui.models.User
import com.example.practiceapplication.ui.viewmodels.UserViewModel
import com.example.practiceapplication.ui.viewmodels.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_controller.*

class ControllerActivity : AppCompatActivity() {

    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val tabNameArray: Array<Int> = arrayOf(R.drawable.ic_home, R.drawable.ic_reservation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        val sharedPref = this.getSharedPreferences(getString(R.string.user_file_key), Context.MODE_PRIVATE)

        val calendarDatabase by lazy { CalendarDatabase.getInstance(this) }
        val calendarRepository by lazy { CalendarRepository(calendarDatabase) }

        val userViewModel = ViewModelProvider(this, UserViewModelFactory(calendarRepository)).get(
            UserViewModel::class.java)

        userViewModel.getUser(sharedPref.getInt(getString(R.string.user_id), 0))

        val userObserver = Observer<User> { currentUser ->
            (currentUser.firstName + " " + currentUser.lastName).also { user.text = it }
        }

        userViewModel.user.observe(this, userObserver)

        logout.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.clear()
            editor.commit()

            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
        }

        tabAdapter = TabAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = tabAdapter

        tabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, tabNameArray[position])
        }.attach()

        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val channelId = "1000"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
