package com.example.weshare20.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Notification
import com.example.weshare20.business.SessionManager
import com.example.weshare20.business.UserGroup
import com.example.weshare20.interfaces.NotificationActionListener

class NotificationActivity : AppCompatActivity(), NotificationActionListener {

    private lateinit var notificationList: MutableList<Notification>
    private lateinit var adapter: NotificationAdapter
    private lateinit var db: DatabaseHandler
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        createNotificationChannel()
        db = DatabaseHandler(this)
        session = SessionManager(this)
        val notifications = db.getUserNotifications(session.getUserId())
        notificationList = notifications.toMutableList()
        adapter = NotificationAdapter(this, notificationList, this)

        val notificationListView: ListView = findViewById(R.id.notificationListView)
        notificationListView.adapter = adapter

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    override fun onAccept(notification: Notification, position: Int) {

        notificationList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun onDecline(notification: Notification, position: Int) {



        notificationList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString("WeShare2.0")
            val descriptionText = getString("WeShare2.0 Notification")
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(1, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
