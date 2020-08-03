package com.example.stockhelper.data

import androidx.room.*

@Dao
interface StockDAO {
    @Query("SELECT * FROM stock")
    fun getAllStock(): List<Stock>

    @Query("DELETE FROM stock")
    fun deleteAllStock()

    @Insert
    fun insertStock(stock: Stock) : Long

    @Delete
    fun deleteStock(stock: Stock)

    @Update
    fun updateStock(stock: Stock)
}