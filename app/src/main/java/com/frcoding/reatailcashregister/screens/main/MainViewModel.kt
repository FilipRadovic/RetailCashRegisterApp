package com.frcoding.reatailcashregister.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcoding.reatailcashregister.models.Item
import com.frcoding.reatailcashregister.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val itemRepository: ItemRepository
): ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    init {
        viewModelScope.launch {
            refreshTrigger
                .onStart { emit(Unit) }
                .flatMapLatest {
                    itemRepository.getAllItems()
                }
                .catch { e -> Log.e("MainViewModel", "Greška: ${e.message}") }
                .collect { loadedItems ->
                    _items.value = loadedItems
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            try {
                itemRepository.insertItem(item)
                refresh()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Greška pri dodavanju: ${e.message}")
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            try {
                if (item.id == null) {
                    itemRepository.insertItem(item)
                    refresh()
                }
                else {
                    itemRepository.updateItem(item)
                    refresh()
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Greška pri izmeni: ${e.message}")
            }
        }
    }

    fun deleteItem(item: Item) {
        try {
            viewModelScope.launch {
                itemRepository.deleteItem(item.id!!)
                refresh()
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Greška pri brisanju: ${e.message}")
        }
    }

    suspend fun deleteAllItems() {
        try {
            itemRepository.deleteAllItems()
            refresh()
        } catch (e: Exception) {
            Log.e("MainViewModel", "Greška pri brisanju: ${e.message}")
        }
    }
}