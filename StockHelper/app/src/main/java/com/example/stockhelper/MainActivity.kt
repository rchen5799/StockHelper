package com.example.stockhelper
//API KEY: CYGT8ODDBYZQQEQY
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stockhelper.adapter.StockAdapter
import com.example.stockhelper.data.AppDatabase
import com.example.stockhelper.data.Stock
import com.example.stockhelper.dialogs.StockDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StockDialog.StockHandler {

    lateinit var stockAdapter: StockAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fab.setOnClickListener { view ->
            showAddStockDialog()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        Thread {
            var stockList = AppDatabase.getInstance(this).stockDao().getAllStock()

            runOnUiThread {
                stockAdapter = StockAdapter(this)
                recyclerList.adapter = stockAdapter

            }
        }.start()
    }

    fun showAddStockDialog() {
        StockDialog()
            .show(supportFragmentManager, getString(R.string.EnterSym))
    }


    fun saveStock(stock: Stock) {
        Thread {
            stock.stockId = AppDatabase.getInstance(this).stockDao().insertStock(stock)

            runOnUiThread {
                stockAdapter.addStock(stock)
            }
        }.start()
    }

    override fun stockCreated(stock: Stock) {
        saveStock(stock)
    }


}