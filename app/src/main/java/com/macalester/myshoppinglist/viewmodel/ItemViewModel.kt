package com.macalester.myshoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.macalester.myshoppinglist.data.AppDatabase
import com.macalester.myshoppinglist.data.Item
import com.macalester.myshoppinglist.data.ItemDAO

class ItemViewModel (application: Application) :
    AndroidViewModel(application) {

    val allItems: LiveData<List<Item>>

    private var itemDAO: ItemDAO

    init {
        itemDAO = AppDatabase.getInstance(application).
        itemDao()
        allItems = itemDAO.getAllItems()
    }

    fun insertItem(item: Item)  {
        Thread {
            itemDAO.addItem(item)
        }.start()
    }

    fun deleteItem(item: Item) {
        Thread {
            itemDAO.deleteItem(item)
        }.start()
    }

    fun deleteAllItems() {
        Thread {
            itemDAO.deleteAllItems()
        }.start()
    }
    fun updateItem(item: Item) {
        Thread {
            itemDAO.updateItem(item)
            }.start()
        }
    }
