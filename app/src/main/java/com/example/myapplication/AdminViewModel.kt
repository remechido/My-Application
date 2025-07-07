/*package com.example.myapplication.Pages

import android.content.Context
import androidx.lifecycle.*
import com.example.myapplication.database.AppDatabase
import User
import com.example.myapplication.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminViewModel(private val dao: UserDao) : ViewModel() {
    val userList = MutableStateFlow<List<User>>(emptyList())

    init {
        viewModelScope.launch {
            dao.getAllUsers().collectLatest {
                userList.value = it
            }
        }
    }

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            dao.insertUser(User(email = email, password = password))
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = AppDatabase.getDatabase(context)
            return AdminViewModel(db.userDao()) as T
        }
    }
}
*/