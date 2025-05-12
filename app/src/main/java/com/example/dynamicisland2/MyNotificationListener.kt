package com.example.dynamicisland2

import android.content.Intent
import android.graphics.Bitmap
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val notification = sbn?.notification

        val title = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_TITLE)?.toString()
        val text = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_TEXT)?.toString()
        val packageName = sbn?.packageName
        var iconBytes: ByteArray? = null


        val titleBig = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_TITLE_BIG)?.toString()
        val subtext = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_SUB_TEXT)?.toString()
        val infotext = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_INFO_TEXT)?.toString()
        val summarytext = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT)?.toString()
        val smallIcon = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_SMALL_ICON)?.toString()
        val largeIcon = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_LARGE_ICON)?.toString()
        val selfDisplayName = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_SELF_DISPLAY_NAME)?.toString()
        val extraMessages = notification?.extras?.getCharSequence(NotificationCompat.EXTRA_MESSAGES)?.toString()
        val notificationIcon = notification?.smallIcon

        Log.d("NotificationDebug", "Title: $title")
        Log.d("NotificationDebug", "Text: $text")
        Log.d("NotificationDebug", "Big Title: $titleBig")
        Log.d("NotificationDebug", "Subtext: $subtext")
        Log.d("NotificationDebug", "Info Text: $infotext")
        Log.d("NotificationDebug", "Summary Text: $summarytext")
        Log.d("NotificationDebug", "Small Icon: $smallIcon")
        Log.d("NotificationDebug", "Large Icon: $largeIcon")
        Log.d("NotificationDebug", "Self Display Name: $selfDisplayName")
        Log.d("NotificationDebug", "Extra Messages: $extraMessages")
        Log.d("NotificationDebug", "Notification SmallIcon: $notificationIcon")


        try {
            val appIcon = packageManager.getApplicationIcon(packageName!!)
            val bitmap = appIcon.toBitmap()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            iconBytes = stream.toByteArray()
        } catch (e: Exception) {
            Log.e("IslandIcon", "Failed to fetch icon: ${e.message}")
        }

        // Send broadcast to DynamicIslandAccessibilityService
        val intent = Intent("com.example.dynamicisland2.NOTIFICATION").apply {
            putExtra("title", title)
            putExtra("text", text)
            putExtra("icon", iconBytes)
            if (packageName == "com.spotify.music"){
                Log.d("dasdfasdfas", "Spotify Detected")
                putExtra("type", "spotify")
            }
        }
        sendBroadcast(intent)
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("000ffaf000", "Notification Removed:")
    }
}