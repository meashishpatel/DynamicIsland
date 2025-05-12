package com.example.dynamicisland2

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import android.widget.TextView

class DynamicIslandAccessibilityService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private lateinit var dynamicIslandView: View
    private var isExpanded = false

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra("title") ?: ""
            val text = intent?.getStringExtra("text") ?: ""
            val type = intent?.getStringExtra("type") ?: ""
            val iconBytes = intent?.getByteArrayExtra("icon")
            Log.d("000ffaf000", "Title: $title, Text: $text, Icon: $iconBytes, Type: $type")
            updateNotification(title, text, iconBytes, type)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        setupDynamicIsland()
        registerReceiver(
            notificationReceiver,
            IntentFilter("com.example.dynamicisland2.NOTIFICATION"),
            RECEIVER_EXPORTED
        )
    }

    private fun setupDynamicIsland() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        dynamicIslandView = LayoutInflater.from(this).inflate(R.layout.dynamic_island_view, null)

        val params = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            format = PixelFormat.TRANSLUCENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }

        windowManager.addView(dynamicIslandView, params)

        // Toggle expand/collapse on whole view tap
        dynamicIslandView.setOnClickListener {
            val previewText = dynamicIslandView.findViewById<TextView>(R.id.notification_preview)
            val expandedLayout = dynamicIslandView.findViewById<View>(R.id.expandedContent)
//            if (isExpanded) collapseIsland() else expandIsland()
            if (isExpanded) {
                // Collapse
                expandedLayout.visibility = View.GONE
                previewText.visibility = View.GONE
                isExpanded = false
            } else {
                // Expand
                previewText.visibility = View.VISIBLE
                expandedLayout.visibility = View.VISIBLE
                isExpanded = true
            }
        }
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
//            val text = event.text.joinToString("\n")
            Log.d("000kkkk0000", event.text.toString())
//            updateNotification(text)
        }
    }

    private fun updateNotification(title: String, text: String, iconBytes: ByteArray?, type: String? = null) {
        runOnUiThread {
            val previewText = dynamicIslandView.findViewById<TextView>(R.id.notification_preview)
            val fullText = dynamicIslandView.findViewById<TextView>(R.id.notificationText)
            val iconView = dynamicIslandView.findViewById<ImageView>(R.id.app_icon)

            previewText.text = if (type == "spotify") "Now Playing: $title" else "Message from: $title"
            fullText.text = text

            if (iconBytes != null) {
                val bmp = BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.size)
                iconView.setImageBitmap(bmp)
                iconView.visibility = View.VISIBLE
            } else {
                iconView.visibility = View.GONE
            }

            previewText.visibility = View.VISIBLE

            // Animate Spotify-specific
            if (type == "spotify") {
                dynamicIslandView.animate()
                    .translationYBy(-10f).alpha(0.8f)
                    .setDuration(200).withEndAction {
                        dynamicIslandView.animate()
                            .translationYBy(10f).alpha(1f)
                            .setDuration(200).start()
                    }.start()
            } else {
                // Normal pulse animation
                dynamicIslandView.animate()
                    .scaleX(1.05f).scaleY(1.05f)
                    .setDuration(100)
                    .withEndAction {
                        dynamicIslandView.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(100).start()
                    }.start()

                Handler(Looper.getMainLooper()).postDelayed({
                    if (!isExpanded) {
                        previewText.visibility = View.GONE
                    }
                }, 2000)
            }
        }
    }

    private fun runOnUiThread(action: () -> Unit) {
        Handler(Looper.getMainLooper()).post(action)
    }

    override fun onInterrupt() {
        windowManager.removeView(dynamicIslandView)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(dynamicIslandView)
    }
}