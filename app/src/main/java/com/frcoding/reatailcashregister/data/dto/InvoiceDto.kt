package com.frcoding.reatailcashregister.data.dto

import com.google.gson.annotations.SerializedName

data class InvoiceDto(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("userId") val userId: Long,
    @SerializedName("method") val paymentMethod: String,
    @SerializedName("total") val totalPrice: Double
)
