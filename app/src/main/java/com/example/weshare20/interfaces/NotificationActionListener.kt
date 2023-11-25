package com.example.weshare20.interfaces

import com.example.weshare20.business.Notification

interface NotificationActionListener {
    fun onAccept(notification: Notification, position: Int)
    fun onDecline(notification: Notification, position: Int)
}
