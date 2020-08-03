package com.example.stockhelper

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stockhelper.adapter.StockAdapter
import com.example.stockhelper.data.StockResult
import com.example.stockhelper.network.StockAPI
import kotlinx.android.synthetic.main.activity_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Summary : AppCompatActivity() {

    lateinit var StockAdapter: StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary) //Ignore this red -- not synced

        StockAdapter = StockAdapter(MainActivity())

        val stockName = intent.getStringExtra(getString(R.string.stock))
        val amount = intent.getStringExtra(getString(R.string.amount))
        val avgCost = intent.getStringExtra(getString(R.string.avgCost))
        var todayPercChange = ""
        var spyPercChange = ""


        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseURL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var stockAPI = retrofit.create(StockAPI::class.java)

        val call = stockAPI.getStockDetails(getString(R.string.GLOBAL_QUOTE), stockName.toString(), getString(R.string.APIKey))

        call.enqueue(object : Callback<StockResult> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<StockResult>, response: Response<StockResult>) {

                var StockResult = response.body()

                tvStock.text = stockName
                tvPrice.text = getString(R.string.PriceLabel) + "${StockResult?.quote?.price}"
                tvBuyPrice.text = getString(R.string.BoughtLabel) + "${avgCost}"

                var price = StockResult?.quote?.price?.toFloat()
                var totalReturns = amount.toFloat() * (price!! - avgCost.toFloat())
                tvAbsDiff.text = getString(R.string.TotalReturnLabel) + "${totalReturns}"

                var PercDiff = (price!! - avgCost.toFloat())/avgCost.toFloat() * 100
                tvPercDiff.text = getString(R.string.TotalPerCLabel) + "${PercDiff}"
                todayPercChange = "${PercDiff}"

                //SPY API Call
                val spycall = stockAPI.getStockDetails(getString(R.string.GLOBAL_QUOTE), getString(R.string.SPY), getString(R.string.APIKey))
                spycall.enqueue(object : Callback<StockResult> {
                    override fun onResponse(spycall: Call<StockResult>, response: Response<StockResult>) {
                        var SpyStockResult = response.body()
                        //SPY price at Jan 2
                        var janSPY = 324.87
                        var spyToday = SpyStockResult?.quote?.price?.toFloat()
                        var spyPerc = (spyToday?.minus(janSPY))?.div(janSPY)?.times(100)
                        tvSPYPerc.text = getString(R.string.SPLabel) + "${spyPerc}"
                        spyPercChange = "${spyPerc}"

                    }
                    override fun onFailure(call: Call<StockResult>, t: Throwable) {
                        tvStock.text = t.message
                    }
                })

            }

            override fun onFailure(call: Call<StockResult>, t: Throwable) {
                tvStock.text = t.message
            }

        })

        var intentCallService = Intent(this, CallService::class.java)
        //Notification Switch
        notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                intentCallService.putExtra(getString(R.string.buy_price), avgCost)
                startService(intentCallService)
            } else {
                stopService(intentCallService)
                Toast.makeText(this, getString(R.string.Blehhhh),
                    Toast.LENGTH_SHORT).show()
            }
        }


        btnAnalysis.setOnClickListener {
            //Takes you to different Activity
            var intentDetails = Intent()
            intentDetails.setClass(this, Analysis::class.java)
            intentDetails.putExtra(getString(R.string.todayPercChange), todayPercChange)
            intentDetails.putExtra(getString(R.string.spyPercChange), spyPercChange)
            startActivity(intentDetails)
        }




    }
}
