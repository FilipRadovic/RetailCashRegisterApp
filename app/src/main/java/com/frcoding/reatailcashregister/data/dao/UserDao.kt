package com.frcoding.reatailcashregister.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frcoding.reatailcashregister.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: User)
    @Query("SELECT * FROM users WHERE username=:username AND password=:password")
    suspend fun loginUser(username: String, password: String): User?
    @Query("SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId: Int): Flow<User>

}