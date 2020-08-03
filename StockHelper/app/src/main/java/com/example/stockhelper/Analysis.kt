package com.example.stockhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_analysis.*

class Analysis : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        val todayPercChange = intent.getStringExtra(getString(R.string.todayPercChange))
        val spyPercChange = intent.getStringExtra(getString(R.string.spyPercChange))

        var stockChange = todayPercChange.toFloat()
        var spyChange = spyPercChange.toFloat()

        if(stockChange > 20 && stockChange < 50) {
            tvTip.text = getString(R.string.over20p1) + getString(R.string.over20p2)
        } else if(stockChange > 50) {
            tvTip.text = getString(R.string.over50)
        } else if(stockChange < 20 && stockChange > 0) {
            tvTip.text = getString(R.string.neutral)
        } else {
            tvTip.text = getString(R.string.bad)
        }

        var diff = stockChange - spyChange

        if(diff >  15) {
            tvComp.text = getString(R.string.GreaterThan15)
        } else if(diff < -10) {
            tvComp.text = getString(R.string.LessThan15)
        } else {
            tvComp.text = getString(R.string.Fiveperc)
        }


    }
}
