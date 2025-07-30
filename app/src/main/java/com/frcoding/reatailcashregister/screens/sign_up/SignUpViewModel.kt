package com.frcoding.reatailcashregister.screens.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcoding.reatailcashregister.models.User
import com.frcoding.reatailcashregister.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val username = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    fun updateUsername(newUsername: String){
        username.value = newUsername
    }

    fun updatePassword(newPassword: String){
        password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String){
        confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick() {
        if (password.value != confirmPassword.value){
            throw Exception("Password do not match")
        }
        val user = User(
            username = username.value,
            password = password.value
        )

        viewModelScope.launch {
            try {
                userRepository.registerUser(user)
            } catch (e: Exception) {
                Log.e("SignUp", "Error registering user: ${e.message}")
            }
        }
    }
}