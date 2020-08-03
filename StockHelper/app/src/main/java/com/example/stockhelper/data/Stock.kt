package com.example.stockhelper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="Stock")

class Stock (
    @PrimaryKey(autoGenerate = true) var stockId : Long?,
    @ColumnInfo(name = "stockName") var stockName: String,
    @ColumnInfo(name = "amount") var amount: String,
    @ColumnInfo(name = "avgCost") var avgCost: String
) : Serializable