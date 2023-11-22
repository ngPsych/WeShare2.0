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

class CreateGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val session = SessionManager(this)
        val db = DatabaseHandler(this)

        val userId = session.getUserId()

        val createGroupButton: Button = findViewById(R.id.createGroupButton)
        val editTextGroupName: EditText = findViewById(R.id.editTextGroupName)
        val editTextDescription: EditText = findViewById(R.id.editTextDescription)

        createGroupButton.setOnClickListener {
            val newGroup = Group(editTextGroupName.text.toString(), editTextDescription.text.toString())
            db.createGroup(newGroup)
            val groupId = db.getCurrentCreateGroupId(newGroup.name, newGroup.description)

            if (groupId != null) {
                db.addUserToGroup(userId, groupId)
            } else {
                print("GroupID is null in CreateGroup.kt line 33")
            }

            val intent = Intent(this, Home::class.java) // Replace YourTargetActivity with the actual class name of your target activity
            startActivity(intent)
        }
    }
}