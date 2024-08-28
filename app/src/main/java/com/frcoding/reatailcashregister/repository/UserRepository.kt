package com.frcoding.reatailcashregister.repository

import com.frcoding.reatailcashregister.data.dao.UserDao
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    suspend fun registerUser(user: User) {
        userDao.registerUser(user)
    }

    suspend fun loginUser(username: String, password: String): User? {
        val user = userDao.loginUser(username, password)
        if (user != null) {
            sessionManager.saveUserId(user.id)
        }
        return user
    }

    fun getUserById(userId: Int): Flow<User> {
        return userDao.getUserById(userId)
    }

    suspend fun logOutUser() {
        val userId = sessionManager.getUserId()
        if (userId != null) {
            sessionManager.clearSession()
        }
    }

}