package com.example.weshare20.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler

class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val db = DatabaseHandler(this)

        val groupName = intent.getStringExtra("GROUP_NAME")
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION")
        Toast.makeText(this, "Welcome to $groupName - $groupDescription", Toast.LENGTH_LONG).show()

        val backButton: Button = findViewById(R.id.backToHomeButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java) // Replace YourTargetActivity with the actual class name of your target activity
            startActivity(intent)
        }

        val addUserButton: Button = findViewById(R.id.addUserButton)
        val editTextViewPhoneNumber: EditText = findViewById(R.id.editTextViewPhoneNumber)
        addUserButton.setOnClickListener {
            val userID = db.getUserIDByPhoneNumber(editTextViewPhoneNumber.text.toString().toInt())
            val groupID = db.getCurrentGroupID(groupName.toString(), groupDescription.toString())
            db.sendGroupInviteNotification(userID, groupID, "You have been invited to join $groupName")
            Toast.makeText(this, "UserID: $userID, GroupID: $groupID", Toast.LENGTH_LONG).show()
        }
    }
}