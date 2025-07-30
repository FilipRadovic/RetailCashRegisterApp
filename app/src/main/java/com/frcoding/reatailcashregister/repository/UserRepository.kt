package com.frcoding.reatailcashregister.repository

import android.util.Log
import com.frcoding.reatailcashregister.data.dao.UserApi
import com.frcoding.reatailcashregister.data.dto.LoginRequest
import com.frcoding.reatailcashregister.data.mappers.toUser
import com.frcoding.reatailcashregister.data.mappers.toUserDto
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.models.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val sessionManager: SessionManager
) {
    suspend fun registerUser(user: User) {
        val response = userApi.registerUser(user.toUserDto())
        if (response.isSuccessful) {
            Log.d("Register", "Uspešno registrovan")
        } else {
            Log.e("Register", "Greška: ${response.code()} - ${response.errorBody()?.string()}")
        }
    }

    suspend fun loginUser(username: String, password: String): User? {
        val loginRequest = LoginRequest(username, password)
        val response = userApi.loginUser(loginRequest)
        return if (response.isSuccessful) {
            val userDto = response.body()
            userDto?.let {
                sessionManager.saveUserId(it.id!!)
                it.toUser()
            }
        } else {
            null
        }
    }
}