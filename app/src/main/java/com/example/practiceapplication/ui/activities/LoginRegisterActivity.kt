package com.example.practiceapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
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

        var emailText = email.text.toString()
        var firstNameText = first_name.text.toString()
        var lastNameText = last_name.text.toString()
        var passwordText = password.text.toString()

        val addObserver = Observer<Long> {
            userViewModel.loginUser(email.text.toString(), password.text.toString())
        }

        userViewModel.confirmUser.observe(this, addObserver)

        val userObserver = Observer<User> { currentUser ->

            Log.i("data", currentUser.toString())

            if (currentUser != null) {
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putInt(getString(R.string.user_id), currentUser.id)
                editor.apply()
                editor.commit()

                val intent = Intent(this, ControllerActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Email or password is incorrect!", Toast.LENGTH_LONG).show()
                password.text.clear()
            }
        }

        userViewModel.user.observe(this, userObserver)

        val emailObserver = Observer<String> { currentEmail ->

            if (currentEmail != null) {
                Log.i("data", currentEmail)
                Toast.makeText(this, "Email already exists!", Toast.LENGTH_LONG).show()
                email.text.clear()
                password.text.clear()
            } else {
                Log.i("data", "no email")
                val user = User(emailText, firstNameText, lastNameText, passwordText)
                userViewModel.addUser(user)
            }
        }

        userViewModel.confirmEmail.observe(this, emailObserver)

        login_or_register.setOnClickListener {
            if (login_or_register.text == getString(R.string.register)) {

                emailText = email.text.toString()
                firstNameText = first_name.text.toString()
                lastNameText = last_name.text.toString()
                passwordText = password.text.toString()

                if (emailText.isBlank() || firstNameText.isBlank() || lastNameText.isBlank() || passwordText.isBlank()){
                    Toast.makeText(this,"Fill in all values!", Toast.LENGTH_LONG).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    Toast.makeText(this, "Email address format is incorrect!", Toast.LENGTH_LONG).show()
                    email.text.clear()
                } else {
                    userViewModel.getEmail(emailText)
                }
            } else {
                userViewModel.loginUser(email.text.toString(), password.text.toString())
            }
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