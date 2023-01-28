package com.macalester.myshoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId : Long?,
    @ColumnInfo(name = "itemTitle") var itemTitle : String,
    @ColumnInfo(name = "itemdesc") var itemDesc : String?,
    @ColumnInfo(name = "price") var price : Float,
    @ColumnInfo(name = "category") var category : Integer,
    @ColumnInfo(name = "isBought") var isBought : Boolean) : java.io.Serializable {
}
