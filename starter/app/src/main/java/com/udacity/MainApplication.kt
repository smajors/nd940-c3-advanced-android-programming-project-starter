package com.udacity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import timber.log.Timber

/**
 * Main entry point for the application. Timber logging is set up here and other
 * long running pre-execution tasks can be set up here as well.
 */
class MainApplication : Application() {
    /**
     * onCreate is called before the application passes execution off to the first activity.
     */
    override fun onCreate() {
        super.onCreate()
        // Plant LineNumberDebugTree
        Timber.plant(LineNumberDebugTree())
        Timber.d("Timber logging initialized.")
        // Create notification channels
        createChannel(CHANNEL_ID, CHANNEL_NAME)
    }


    private fun createChannel(channelId: String, channelName: String) {
        // Check for Oreo or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            channel.apply {
                enableLights(true)
                lightColor = Color.WHITE
                enableVibration(true)
                description = getString(R.string.download_channel_desc)
                vibrationPattern = longArrayOf(2,3,2,4)
            }
            // Create notification channel
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

    }
    /**
     * Main class that is used by this application's Timber logging
     * Logs in the format:
     *
     * Level/(Filename.kt:LineNumber)#function: LogString
     */
    class LineNumberDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
            return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
    }

    companion object {
        const val CHANNEL_ID = "testDownloadChannelId"
        const val CHANNEL_NAME = "DownloadStatus"
    }
}