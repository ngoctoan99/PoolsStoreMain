package com.pools.store.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.pools.store.R
import com.pools.store.data.local.CachePreferencesHelper
import com.pools.store.fcm.NotificationUtil.CHANNEL_ID_DEFAULT
import com.pools.store.fcm.NotificationUtil.CHANNEL_NAME_DEFAULT
import timber.log.Timber


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        Timber.i("[MyFirebaseMessagingService] onNewToken(token: $token)")

        val preferencesHelper = CachePreferencesHelper(applicationContext)
        preferencesHelper.fcmToken = token

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("TTT onMessageReceived","${message}")
        super.onMessageReceived(message)
        try {
            val messageData = parseDataPayLoad(message)
            if (messageData != MessageData.empty()) {
                createNotificationWithDialog(
                    this@MyFirebaseMessagingService,
                    messageData.dataObject
                )
            }

        } catch (ex: Exception) {
            Log.d("TTT MessageReceivedError","$ex")
        }


    }

    private fun parseDataPayLoad(message: RemoteMessage): MessageData {
        return try {
            var dataObject = ""
            message.data["json"]?.let {
                Timber.i("message data $it")
                if (it.isNotEmpty()) {
                    dataObject = it
                }
            }

            MessageData(dataObject)
        } catch (ex: Exception) {
            MessageData.empty()
        }
    }


    private fun createNotificationWithDialog(
        mContext: Context,
        dataObject: String
    ) {
        var title = ""
        var message = ""
        val dataNotificationDefault = Gson().fromJson(
            dataObject,
            DataDefaultNotification::class.java
        )
        title = dataNotificationDefault.name
        message = dataNotificationDefault.description
        createNotificationChannel(nameChannel = CHANNEL_NAME_DEFAULT, CHANNEL_ID_DEFAULT)
        createNotification(title = title, body = message)
    }
//    // Inside MyFirebaseMessagingService class
    private fun createNotification(title: String?, body: String?) {
        // Create a notification channel (required for Android 8.0 and above)

//        val intent = Intent(this, FirstActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )

        // Build the notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_app)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Add any additional customizations to the builder based on custom data
        // For example, you can set a custom icon, actions, etc.

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun createNotificationChannel(nameChannel: String, idChannel: String) {
        // Create a notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "Project Pools Store of Playground"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(idChannel, nameChannel, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    data class MessageData(
        val dataObject: String = ""
    ) {
        companion object {
            fun empty() = MessageData("")
        }
    }
}