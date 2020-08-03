package com.example.stockhelper.data

import com.google.gson.annotations.SerializedName

//result generated from /json
data class StockResult(@SerializedName("Global Quote") val quote: Global_Quote?)

data class Global_Quote(@SerializedName("01. symbol") val symbol: String?,
                        @SerializedName("02. open") val open: String?,
                        @SerializedName("03. high") val high: String?,
                        @SerializedName("04. low") val low: String?,
                        @SerializedName("05. price") val price: String?,
                        @SerializedName("06. volume") val volume: String?,
                        @SerializedName("07. latest trading day") val latest_trading_day: String?,
                        @SerializedName("08. previous close") val previous_close: String?,
                        @SerializedName("09. change") val change: String?,
                        @SerializedName("10. change_percent") val change_percent: String?)
