package com.example.weshare20.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.weshare20.R
import com.example.weshare20.business.Notification
import com.example.weshare20.interfaces.NotificationActionListener

class NotificationAdapter(context: Context, notifications: List<Notification>, private val actionListener: NotificationActionListener) :
    ArrayAdapter<Notification>(context, 0, notifications) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_notification, parent, false)
        val notification = getItem(position)

        val notificationTextView = view.findViewById<TextView>(R.id.notificationLayout)
        notificationTextView.text = notification?.message

        val acceptButton = view.findViewById<Button>(R.id.acceptButton)
        acceptButton.setOnClickListener {
            notification?.let { it1 -> actionListener.onAccept(it1, position) }
        }

        val declineButton = view.findViewById<Button>(R.id.declineButton)
        declineButton.setOnClickListener {
            notification?.let { it1 -> actionListener.onDecline(it1, position) }
        }

        return view
    }
}
