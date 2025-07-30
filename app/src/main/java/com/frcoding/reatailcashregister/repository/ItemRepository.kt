package com.frcoding.reatailcashregister.repository

import com.frcoding.reatailcashregister.data.dao.ItemApi
import com.frcoding.reatailcashregister.data.mappers.toItem
import com.frcoding.reatailcashregister.data.mappers.toItemDto
import com.frcoding.reatailcashregister.models.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemApi: ItemApi
) {
    suspend fun getAllItems(): Flow<List<Item>> = flow {
        val response = itemApi.getAllItems()
        if (response.isSuccessful) {
            emit(response.body()?.map { it.toItem() } ?: emptyList())
        } else {
            throw Exception("Error response: ${response.code()}")
        }
    }

    suspend fun insertItem(item: Item): Item {
        val response = itemApi.insertItem(item.toItemDto())
        if (response.isSuccessful) {
            return response.body()?.toItem() ?: throw Exception("No item found.")
        } else {
            throw Exception("Error response: ${response.code()}")
        }
    }

    suspend fun updateItem(item: Item): Item {
        val response = itemApi.updateItem(item.toItemDto())
        if (response.isSuccessful) {
            return response.body()?.toItem() ?: throw Exception("No item found.")
        } else {
            throw Exception("Error response: ${response.code()}")
        }
    }

    suspend fun updateItems(items: List<Item>): List<Item> {
        val response = itemApi.updateItems(items.map { it.toItemDto() })
        if (response.isSuccessful) {
            return response.body()?.map { it.toItem() } ?: emptyList()
        } else {
            throw Exception("Error response: ${response.code()}")
        }
    }

    suspend fun deleteItem(id: Long) = itemApi.deleteItem(id)

    suspend fun deleteAllItems() = itemApi.deleteAllItems()
}