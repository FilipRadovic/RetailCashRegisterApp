package com.frcoding.reatailcashregister.data.dao

import com.frcoding.reatailcashregister.data.dto.LoginRequest
import com.frcoding.reatailcashregister.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("users/register")
    suspend fun registerUser(@Body user: UserDto): Response<Unit>

    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<UserDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UserDto>
}