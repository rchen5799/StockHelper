package com.example.stockhelper

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.stockhelper.data.StockResult
import com.example.stockhelper.network.StockAPI
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep
import java.util.*


class CallService : Service() {

    //Variables to let user know that the app is running (Foreground service prevents quitting)
    private val NOTIFICATION_CHANNEL_ID = "call_service_notification"
    private val NOTIFICATION_CHANNEL_NAME = "Call Service Notification"
    private val NOTIF_FOREGROUND_ID = 101

    //This makes it so it doesn't need to return anything
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var enabled = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!enabled) {
            enabled = true

            val handlerUIThread = Handler(
                Looper.getMainLooper()
            )

            //Starts Foreground to prevent background from closing
            startForeground(NOTIF_FOREGROUND_ID, getMyNotification(getString(R.string.updateAPI)))

            val stockName = intent?.getStringExtra(getString(R.string.stock))
            val buy_price = intent?.getStringExtra(getString(R.string.buy_price))

            class runnable(stock: String) : Runnable {
                //Starts the Thread
                //stockName: String?;
                var stockName = stock

                override fun run() {
                    while (enabled) {
                        //TODO: FIND A WAY TO PASS PARAMETERS TO RUNNABLE (stockName)
                        var stockName = "IBM"

                        val retrofit = Retrofit.Builder()
                            .baseUrl(getString(R.string.baseURL))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                        var stockAPI = retrofit.create(StockAPI::class.java)

                        val call = stockAPI.getStockDetails(
                            getString(R.string.GLOBAL_QUOTE),
                            stockName.toString(),
                            getString(R.string.APIKey)
                        )

                        call.enqueue(object : Callback<StockResult> {
                            override fun onResponse(
                                call: Call<StockResult>,
                                response: Response<StockResult>
                            ) {

                                var StockResult = response.body()

                                var price = StockResult?.quote?.price?.toFloat()
                                var avgCost = buy_price?.toFloat()
                                var perReturns = (price?.minus(avgCost!!))?.div(avgCost!!)?.times(100)

                                if (perReturns != null) {
                                    if(perReturns > 20) {
                                        //UI Thread Running
                                        handlerUIThread.post {
                                            Toast.makeText(
                                                this@CallService,
                                                "$stockName Over 20% Returns: $perReturns%. Think about selling! View Suggestions for More Info.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else if (perReturns < -10) {
                                        handlerUIThread.post {
                                            Toast.makeText(
                                                this@CallService,
                                                "Warning $stockName -10% Returns $perReturns%. Proceed with caution!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                    updateNotification(stockName)
                                }
                            }

                            override fun onFailure(call: Call<StockResult>, t: Throwable) {
                                stopSelf()
                            }

                        })

                        sleep(15000)
                    }
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }

            val thread = Thread(stockName?.let { runnable(it) })
            thread.start()
        }
        return START_STICKY
    }

    private fun updateNotification(text: String) {
        val notification = getMyNotification(text)
        val notifMan = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notifMan?.notify(NOTIF_FOREGROUND_ID, notification)
    }

    private fun getMyNotification(text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this,
            NOTIF_FOREGROUND_ID,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(
            this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notRunning))
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(contentIntent).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        enabled = false
        stopForeground(false)
        super.onDestroy()
    }


}
