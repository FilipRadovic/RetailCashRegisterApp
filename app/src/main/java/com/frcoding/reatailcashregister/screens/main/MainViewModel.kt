package com.frcoding.reatailcashregister.screens.main

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcoding.reatailcashregister.models.Item
import com.frcoding.reatailcashregister.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val itemRepository: ItemRepository
): ViewModel() {
    //Flow for list of items
    val items = itemRepository.getAllItems()

    fun addItem(item: Item) {
        viewModelScope.launch {
            itemRepository.insertItem(item)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            if (item.id == null) {
                itemRepository.insertItem(item)
            }
            else {
                itemRepository.updateItem(item)
            }
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemRepository.deleteItem(item)
        }
    }

    fun deleteAllItems() {
        viewModelScope.launch {
            itemRepository.deleteAllItems()
        }
    }

}