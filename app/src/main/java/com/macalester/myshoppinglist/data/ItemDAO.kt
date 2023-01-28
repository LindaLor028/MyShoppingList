package com.macalester.myshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDAO {

    @Query("SELECT * FROM item")
    fun getAllItems() : LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE itemTitle LIKE '%' || :text || '%'")
    fun findItems(text: String) : LiveData<List<Item>>

    @Insert
    fun addItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("DELETE FROM item")
    fun deleteAllItems()

    @Update
    fun updateItem(item: Item)
}