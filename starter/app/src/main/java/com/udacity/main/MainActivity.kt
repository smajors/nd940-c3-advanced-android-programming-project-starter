package com.udacity.main

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.R
import com.udacity.button.ButtonState
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.DownloadStatusAttributes
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var URL: String

    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate() of MainActivity")
        // Get binding object and set the view
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(toolbar)

        // Set listener for the custom button
        binding.contentMain.loadButton.setOnClickListener {
            Timber.d("Download button clicked.")
            download()
        }

        binding.contentMain.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.id_glide -> URL = GLIDE_URL
                R.id.id_loadapp -> URL = LOADAPP_URL
                R.id.id_retrofit -> URL = RETROFIT_URL
            }
            Timber.d("Check changed. URL set to $URL")
        }

        // Register callback receiver
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // Create download manager
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.d("File download complete.")
            // File's done. Set button state to completed.
            binding.contentMain.loadButton.setState(ButtonState.Completed)
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            // Check to see if an error occurred.
            val query = DownloadManager.Query()
            query.setFilterById(id!!)
            val cursor = downloadManager.query(query)
            // Set downloaded file
            val downloadedFile: String = when (URL) {
                LOADAPP_URL -> {
                    getString(R.string.download_loadapp)
                }
                GLIDE_URL -> {
                    getString(R.string.download_glide)
                }
                RETROFIT_URL -> {
                    getString(R.string.download_retrofit)
                }
                else -> {
                    // This should never happen
                    "null"
                }
            }
            // Set initial status to failed
            var success = false
            if (cursor.moveToFirst()) {
                val colIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(colIndex)

                // We don't actually need to do much with these statuses. We just need to show that
                // the file was not successfully downloaded in DetailActivity
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    success = true
                }
            }

            // We now have the data we need. Create the notification
            val attrs = DownloadStatusAttributes(downloadedFile, success)

            val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            notificationManager.sendNotification(attrs, applicationContext)
        }
    }

    /**
     * Downloads a file based on the selection given by the user from the radio group
     */
    private fun download() {
        // Set click state. As of now this does nothing but it is handled internally
        binding.contentMain.loadButton.setState(ButtonState.Clicked)
        try {
            // Parse will throw an exception if a selection has not been made
            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            // Set button loading state
            binding.contentMain.loadButton.setState(ButtonState.Loading)
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
        catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(this, getString(R.string.download_error), Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        private const val LOADAPP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"

    }

}
