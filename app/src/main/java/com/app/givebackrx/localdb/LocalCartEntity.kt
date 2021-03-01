package com.app.givebackrx.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class LocalCartEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "imageLogo") var imageLogo: String,
    @ColumnInfo(name = "product_id") var product_id: String,
    @ColumnInfo(name = "quantity") var quantity: String,
    @ColumnInfo(name = "size") var size: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "createdAt") var createdAt: Long = Calendar.getInstance().timeInMillis
)
