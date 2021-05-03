package com.udacity.detail

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.main.MainActivity
import com.udacity.utils.DATA_EXTRAS
import com.udacity.utils.DownloadStatusAttributes
import com.udacity.utils.NOTIFICATION_ID
import kotlinx.android.synthetic.main.activity_detail.*
import timber.log.Timber
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate() of DetailActivity")
        // Get binding object and set the view
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(toolbar)

        // Cancel notification
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID)

        // The intent should always be there and the extras should be attached
        val obj = intent!!.getSerializableExtra(DATA_EXTRAS) as DownloadStatusAttributes
        // Set download type
        binding.contentDetail.textviewFilename.text = String.format(Locale.getDefault(),
            getString(R.string.detail_filename), obj!!.downloadType)
        // Set download success
        binding.contentDetail.textviewSuccess.text = String.format(Locale.getDefault(),
            getString(R.string.detail_success), obj.success)

        // Set on click listener for Okay button
        binding.contentDetail.okayButton.setOnClickListener {
            Timber.d("Okay button clicked. Going back to MainActivity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



}
