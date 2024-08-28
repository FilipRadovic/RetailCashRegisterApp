package com.frcoding.reatailcashregister.screens.sign_in

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.models.User
import com.frcoding.reatailcashregister.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _loginResult = MutableStateFlow<User?>(null)
    val loginResult: StateFlow<User?> = _loginResult

    fun updateUsername(newUsername: String) {
        username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick() {
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser(username.value, password.value)
                if (user != null) {
                    sessionManager.saveUserId(user.id)
                    _loginResult.value = user
                    // Navigacija na glavni ekran nakon prijave
                } else {
                    // Handle login failure, e.g., show an error message
                }
            } catch (e: Exception) {
                Log.e("SignIn", "Error during sign in: ${e.message}")
            }
        }
    }

}