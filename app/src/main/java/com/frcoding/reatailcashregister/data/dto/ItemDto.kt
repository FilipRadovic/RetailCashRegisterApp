package com.frcoding.reatailcashregister.data.dto

import com.google.gson.annotations.SerializedName

data class ItemDto(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("price") val price: Double
)
