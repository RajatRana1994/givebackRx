package com.app.givebackrx.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class FreqSearchEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "display_drug_name") var display_drug_name: String,
    @ColumnInfo(name = "drug_generic_name") var drug_generic_name: String,
    @ColumnInfo(name = "drug_name") var drug_name: String,
    @ColumnInfo(name = "createdAt") var createdAt: Long = Calendar.getInstance().timeInMillis
)
