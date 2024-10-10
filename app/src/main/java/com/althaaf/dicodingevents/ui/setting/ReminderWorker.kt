package com.althaaf.dicodingevents.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.althaaf.dicodingevents.R
import com.althaaf.dicodingevents.data.retrofit.ApiConfig
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return fetchEventData()
    }

    private fun fetchEventData(): Result {
        Log.d(TAG, "getEvent: Mulai.....")
        try {
            val response = ApiConfig.getApiService().getListEvents(active = 1, limit = 1, q = null).execute()
            if (response.isSuccessful){
                val responseBody = response.body()
                if(responseBody != null) {
                    val event = responseBody.listEvents
                    if(!event.isNullOrEmpty()) {
                            resultStatus = Result.success()
                            showNotification(event[0].name, event[0].beginTime)
                            Log.d(TAG, "onSucces: Selesai.....")
                    } else {
                        resultStatus = Result.failure()
                        Log.d(TAG, "onSucces: kosong.....")
                    }
                }
            } else {
                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                showNotification("Data event tidak berhadil didapatkan", response.body()?.message)
                Log.d(TAG, "onSuccess: Gagal.....")
                resultStatus = Result.failure()
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            showNotification("Data event gagal diambil", e.message)
            Log.d(TAG, "onSuccess: Gagal.....")
            resultStatus = Result.failure()
        }

        return resultStatus as Result
    }

    private fun showNotification(name: String?, beginTime: String?) {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(beginTime!!)
        val df = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).format(dateFormat!!)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.event_upcoming)
            .setContentTitle(name)
            .setContentText("Kegiatan akan dimulai pada $df, jangan sampai terlewatkan ya!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText("Event Reminder")
            .setAutoCancel(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "ReminderWorker"
        const val NOTIFICATION_ID = 120
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

}