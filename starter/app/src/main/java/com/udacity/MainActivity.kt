package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

var downloadManager: DownloadManager? = null
lateinit var notificationManager: NotificationManager

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var NOTIFICATION_ID = 0
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private  var url: String=""
    private lateinit var fileName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_id)
        )
        val result: RadioGroup =findViewById(R.id.radio_group)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        result.setOnCheckedChangeListener{ _: RadioGroup, index: Int ->
    when((findViewById<RadioButton>(index)).text.toString()){
        getString( R.string.choice_1)->{
            url=getString(R.string.Glide)
        }
        getString( R.string.choice_2)->{

            url=getString(R.string.Repository)

        }

        getString( R.string.choice_3)->{

            url=getString(R.string.Retrofit)

        }

    }

}
        custom_button.setOnClickListener {
            if(url.isNotEmpty()){
            if(downloadID==0L){
                when(url){
                    getString(R.string.Glide)-> fileName=getString( R.string.choice_1)
                    getString(R.string.Repository)-> fileName=getString( R.string.choice_2)

                    getString(R.string.Retrofit)-> fileName=getString( R.string.choice_3)

                }

                download()

            }
            }else{
                custom_button.DownloadCompleted()
                val toast=Toast(applicationContext)
                toast.setText("please choose a link")
                toast.show()
            }

        }

    }


    fun NotificationManager.sendNotification(messageBody: List<String>, applicationContext: Context) {
        val contentIntent = Intent(applicationContext, DetailActivity::class.java)
            .putExtra("name",messageBody[0])
            .putExtra("status",messageBody[1])


        pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.download_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext
                .getString(R.string.notification_title))
            .setContentText(messageBody[0])
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                applicationContext.getString(R.string.action_settings),
                pendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notify(NOTIFICATION_ID, builder.build())
        NOTIFICATION_ID++

    }
     private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id!=downloadID){
                return
            }
            val x=downloadManager!!.query(
                DownloadManager.Query()
                    .setFilterById(downloadID)
            )
            x.moveToFirst()
            val status=when(x.getInt(x.getColumnIndex(DownloadManager.COLUMN_STATUS))){
                DownloadManager.STATUS_SUCCESSFUL->true
                DownloadManager.STATUS_FAILED->false

                else -> false
            }
            if (context != null) {
                notificationManager.sendNotification(
                   listOf(fileName,status.toString()) ,
                    context
                )
                custom_button.DownloadCompleted()
            }

            downloadID=0L
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationUri(Uri.parse("file:///storage/emulated/0/Download/master.zip"))



         downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager!!.enqueue(request)// enqueue puts the download request in the queue.

        Log.i("testtt-dd",downloadID.toString())
    }
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"
             notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }}
    companion object {
        private const val CHANNEL_ID = "channelId"
    }

}
