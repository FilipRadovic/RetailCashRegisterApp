package com.frcoding.reatailcashregister.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "invoices")
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: Int,

    val totalPrice: Double,

    val paymentMethod: String
)
