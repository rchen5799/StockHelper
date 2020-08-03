package com.example.stockhelper.network

import com.example.stockhelper.data.StockResult
import retrofit2.http.GET
import retrofit2.http.Query

//https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo

// HOST:    https://www.alphavantage.co
// PATH:    /query?
// QUERY:   function=GLOBAL_QUOTE&symbol=IBM&apikey=CYGT8ODDBYZQQEQY

interface StockAPI {
    @GET("/query?")
    fun getStockDetails(@Query("function") function: String,
                          @Query("symbol") symbol: String,
                          @Query("apikey") apikey: String): retrofit2.Call<StockResult>


}