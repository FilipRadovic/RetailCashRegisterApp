package com.frcoding.reatailcashregister.models

data class Invoice(
    val id: Long? = null,
    val userId: Long,
    val paymentMethod: String,
    val totalPrice: Double
)
