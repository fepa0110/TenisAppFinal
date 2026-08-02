package com.example.tenisapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tenisappf.model.UserRole
import com.example.tenisappf.model.firebase.Player
import com.example.tenisappf.model.firebase.User

import kotlinx.coroutines.delay

class NewPlayerViewModel() : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _genre = MutableLiveData<String>()
    val genre: LiveData<String> = _genre

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onPlayerChanged(name: String, genre: String) {
        _name.value = name
        _genre.value = genre
    }

    private fun isValidPassword(password: String): Boolean = password.length > 3

    private fun isValidUsername(name: String): Boolean  = name.length > 3


}