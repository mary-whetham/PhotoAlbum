package com.example.practiceapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.practiceapplication.R
import com.example.practiceapplication.data.CalendarDatabase
import com.example.practiceapplication.data.repository.CalendarRepository
import com.example.practiceapplication.ui.models.User
import com.example.practiceapplication.ui.viewmodels.UserViewModel
import com.example.practiceapplication.ui.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        val sharedPref = this.getSharedPreferences(getString(R.string.user_file_key), Context.MODE_PRIVATE)

        val calendarDatabase by lazy { CalendarDatabase.invoke(this) }
        val calendarRepository by lazy { CalendarRepository(calendarDatabase) }

        val userViewModel = ViewModelProvider(this, UserViewModelFactory(calendarRepository)).get(UserViewModel::class.java)

        val userObserver = Observer<User> { currentUser ->
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt(getString(R.string.user_id), currentUser.id)
            editor.apply()
            editor.commit()

            val intent = Intent(this, ControllerActivity::class.java)
            startActivity(intent)
        }

        userViewModel.user.observe(this, userObserver)

        login_or_register.setOnClickListener {
            if (login_or_register.text == getString(R.string.register)) {
                val user = User(email.text.toString(), first_name.text.toString(), last_name.text.toString(), password.text.toString())
                userViewModel.addUser(user)
            }

            userViewModel.loginUser(email.text.toString(), password.text.toString())
        }

        switch_login_reg.setOnClickListener{
            if (switch_login_reg.text == getString(R.string.to_login)) {
                login_reg_title.text = getString(R.string.login)
                first_name_block.visibility = View.GONE
                last_name_block.visibility = View.GONE
                login_or_register.text = getString(R.string.login)
                switch_login_reg.text = getString(R.string.to_register)
            } else if (switch_login_reg.text == getString(R.string.to_register)) {
                login_reg_title.text = getString(R.string.register)
                first_name_block.visibility = View.VISIBLE
                last_name_block.visibility = View.VISIBLE
                login_or_register.text = getString(R.string.register)
                switch_login_reg.text = getString(R.string.to_login)
            }
        }
    }
}