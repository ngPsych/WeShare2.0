package com.example.weshare20.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Group
import com.example.weshare20.business.SessionManager
import com.example.weshare20.business.UserGroup

class CreateGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val session = SessionManager(this)
        val db = DatabaseHandler(this)
        createNotificationChannel()

        val userId = session.getUserId()

        val createGroupButton: Button = findViewById(R.id.createGroupButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)
        val editTextGroupName: EditText = findViewById(R.id.editTextGroupName)
        val editTextDescription: EditText = findViewById(R.id.editTextDescription)

        createGroupButton.setOnClickListener {
            val newGroup = Group(editTextGroupName.text.toString(), editTextDescription.text.toString())
            db.createGroup(newGroup)
            val groupID = db.getCurrentGroupID(newGroup.name, newGroup.description)
            val newUserGroup = UserGroup(userId, groupID)
            db.addUserToGroup(newUserGroup)
            print(newUserGroup)


            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
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