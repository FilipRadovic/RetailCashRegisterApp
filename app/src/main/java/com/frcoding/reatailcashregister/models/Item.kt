package com.frcoding.reatailcashregister.models

data class Item(
    val id: Long? = null,
    val name: String,
    val quantity: String,
    val price: Double
)
