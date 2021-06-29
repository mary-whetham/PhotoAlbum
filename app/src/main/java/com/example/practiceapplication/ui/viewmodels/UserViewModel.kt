package com.example.practiceapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapplication.data.repository.CalendarRepository
import com.example.practiceapplication.ui.models.User
import kotlinx.coroutines.launch

class UserViewModel(
    private val calendarRepository: CalendarRepository
): ViewModel() {

    var user = MutableLiveData<User>()

    fun getUser(id: Int) {
        viewModelScope.launch {
            try {
                user.postValue(calendarRepository.getUser(id))
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                user.postValue(calendarRepository.loginUser(email, password))
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                calendarRepository.insertUser(user)
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }
    }

}