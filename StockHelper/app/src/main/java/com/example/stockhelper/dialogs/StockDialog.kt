package com.example.stockhelper.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.stockhelper.R
import com.example.stockhelper.data.Stock

import kotlinx.android.synthetic.main.stock_dialog.view.*

class StockDialog : DialogFragment() {
    interface StockHandler {
        fun stockCreated(stock: Stock)
    }

    lateinit var stockHandler: StockHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is StockHandler) {
            stockHandler = context
        } else {
            throw RuntimeException(getString(R.string.Runtime_Exception)) as Throwable
        }
    }

    lateinit var etStockName: EditText
    lateinit var etAmt: EditText
    lateinit var etAvgCost: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Stock Dialog")
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.stock_dialog, null
        )

        etStockName = dialogView.etStockName
        etAmt = dialogView.etAmt
        etAvgCost = dialogView.etAvgCost


        dialogBuilder.setView(dialogView)

        val arguments = this.arguments

        dialogBuilder.setPositiveButton(getString(R.string.Ok)) {
                dialog, which ->
        }
        dialogBuilder.setNegativeButton(getString(R.string.Cancel)) {
                dialog, which ->
        }


        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etStockName.text.isNotEmpty() && etAmt.text.isNotEmpty() && etAvgCost.text.isNotEmpty()) {
                handleStockCreate()
                dialog!!.dismiss()
            } else {
                etStockName.error = getString(R.string.nameError)
                etAmt.error = getString(R.string.AmtError)
                etAvgCost.error = getString(R.string.CostError)
            }
        }
    }

    private fun handleStockCreate() {
        stockHandler.stockCreated(
            Stock(
                null,
                etStockName.text.toString(),
                etAmt.text.toString(),
                etAvgCost.text.toString()
            )
        )
    }


}