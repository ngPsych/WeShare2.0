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

    private val db
    private val session
    private val notifications
    private val adapter
    private lateinit var notificationList: MutableList<Notification> // Declare as MutableList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        db = DatabaseHandler(this)
        session = SessionManager(this)
        notifications = db.getUserNotifications(session.getUserId()) // get your list of notifications
        adapter = NotificationAdapter(this, notifications, this)

        val notificationListView: ListView = findViewById(R.id.notificationListView)
        notificationListView.adapter = adapter
    }

    override fun onAccept(notification: Notification, position: Int) {
        // Now you have the position, you can retrieve the notification directly
        db.addUserToGroup(UserGroup(notification?.userID, notification?.groupID))
        db.deleteNotification(notification?.userID, notification?.groupID, notification?.message)
        // You may want to remove the notification from the list and update the adapter
        notificationList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun onDecline(notification: Notification, position: Int) {
        // Handle decline action
        db.deleteNotification(notification?.userID, notification?.groupID, notification?.message)
        // Remove the notification from the list and update the adapter
        notificationList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

}