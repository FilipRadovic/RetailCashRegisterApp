package com.frcoding.reatailcashregister.data.dao

import com.frcoding.reatailcashregister.data.dto.ItemDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ItemApi {
    @GET("items")
    suspend fun getAllItems(): Response<List<ItemDto>>

    @POST("items")
    suspend fun insertItem(@Body item: ItemDto): Response<ItemDto>

    @PUT("items")
    suspend fun updateItem(@Body item: ItemDto): Response<ItemDto>

    @PUT("items/bulk")
    suspend fun updateItems(@Body items: List<ItemDto>): Response<List<ItemDto>>

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Long): Response<Unit>

    @DELETE("items")
    suspend fun deleteAllItems(): Response<Unit>
}