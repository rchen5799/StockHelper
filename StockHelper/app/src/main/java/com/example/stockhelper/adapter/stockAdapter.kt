package com.example.stockhelper.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockhelper.MainActivity
import com.example.stockhelper.R
import com.example.stockhelper.Summary
import com.example.stockhelper.data.AppDatabase
import com.example.stockhelper.data.Stock
import kotlinx.android.synthetic.main.stock_row.view.*

class StockAdapter(val context:MainActivity) : RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    var stockItems = mutableListOf<Stock>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.stock_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stockItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.parseColor(context.getString(R.string.lightBlue)));
        val currentStock = stockItems[position]

        holder.tvStockName.text = currentStock.stockName
        holder.tvAmt.text = context.getString(R.string.AmtLabel) + "${currentStock.amount}"
        holder.tvAvgCost.text = context.getString(R.string.AvgCostLabel) + "${currentStock.avgCost}"

        holder.btnDelete.setOnClickListener {
            deleteStock(holder.adapterPosition)
        }

        holder.btnDetails.setOnClickListener {
            //Takes you to different Activity

            var intentDetails = Intent()

            intentDetails.setClass(context, Summary::class.java)
            intentDetails.putExtra(context.getString(R.string.stock), currentStock.stockName)
            intentDetails.putExtra(context.getString(R.string.amount), currentStock.amount)
            intentDetails.putExtra(context.getString(R.string.avgCost), currentStock.avgCost)


            context.startActivity(intentDetails)
        }
    }

    private fun deleteStock(position: Int) {
        Thread {
            AppDatabase.getInstance(context).stockDao().deleteStock(
                stockItems.get(position)
            )

            (context as MainActivity).runOnUiThread {
                stockItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    public fun addStock(stock: Stock) {
        stockItems.add(stock)

        notifyItemInserted(stockItems.lastIndex)
    }

    inner class ViewHolder(stockView: View) : RecyclerView.ViewHolder(stockView) {
        val tvStockName = stockView.tvStockName
        val tvAmt = stockView.tvAmt
        val tvAvgCost = stockView.tvAvgCost
        val btnDelete = stockView.btnDelete
        val btnDetails = stockView.btnDetails
    }

}