package com.frcoding.reatailcashregister.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
