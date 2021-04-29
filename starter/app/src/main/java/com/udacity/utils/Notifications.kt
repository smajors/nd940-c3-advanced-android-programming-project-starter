package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.VectorDrawable
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.udacity.MainApplication
import com.udacity.R
import com.udacity.detail.DetailActivity
import com.udacity.main.MainActivity
import timber.log.Timber

private const val NOTIFICATION_ID = -65535

data class DownloadStatusAttributes(val downloadType: String, val success: Boolean)
/**
 * Sends a notification to the Android slide down message area with an intent
 */
fun NotificationManager.sendNotification(obj: DownloadStatusAttributes, context: Context) {
    Timber.d("Creating notification")
    // Create intent and pending intent
    Timber.d("Main intent creation")
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Create image and style it to big picture icon
    Timber.d("Big picture notification icon")

    val image = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_cloud_download_24, null) as VectorDrawable
    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(image.toBitmap())
        .bigLargeIcon(null)

    // Set action intent for button in intent
    Timber.d("Action intent creation")
    val actionIntent = Intent(context, DetailActivity::class.java)
    val actionPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Build notification
    val builder = NotificationCompat.Builder(context, MainApplication.CHANNEL_ID)

    Timber.d("Creating Notification Builder")
    builder.apply {
        setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
        setContentTitle(context.getString(R.string.download_finished))
        setContentText("${obj.downloadType} has finished downloading!")
        setContentIntent(pendingIntent)
        setAutoCancel(true)
        setStyle(bigPictureStyle)
        setLargeIcon(image.toBitmap())
        addAction(R.drawable.ic_assistant_black_24dp, context.getString(R.string.check_status), actionPendingIntent)
        priority = NotificationCompat.PRIORITY_HIGH
    }

    // Display notification
    notify(NOTIFICATION_ID, builder.build())
}