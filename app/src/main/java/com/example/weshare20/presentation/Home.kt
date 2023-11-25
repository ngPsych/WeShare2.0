package com.example.weshare20.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.R
import com.example.weshare20.business.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val session = SessionManager(this)
        val db = DatabaseHandler(this)

        val userId = session.getUserId()

        val profilePicImageView: ImageView = findViewById(R.id.homeProfilePic)
        val notificationButton: ImageView = findViewById(R.id.notificationButton)
        val logOutButton: Button = findViewById(R.id.logOutButton)
        val addGroupButton: FloatingActionButton = findViewById(R.id.addGroupButton)

        val groupListView: ListView = findViewById(R.id.groupListView)
        val groups = db.getUserGroups(userId) // get your list of groups from the database
        val adapter = GroupAdapter(this, groups)
        groupListView.adapter = adapter

        if (userId != -1) {
            // User is logged in
            val user = db.getUserInfo(userId)

            if (user != null) {
                val fullname = user.fullname
                Toast.makeText(this, "Hi $fullname", Toast.LENGTH_LONG).show()

                logOutButton.setOnClickListener {
                    session.clearSession()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                groupListView.setOnItemClickListener { parent, view, position, id ->
                    val group = adapter.getItem(position)
                    // Now you have access to group.name and group.description without using tags

                    // Intent to start a new activity, passing group details
                    val intent = Intent(this, GroupActivity::class.java).apply {
                        putExtra("GROUP_NAME", group?.name)
                        putExtra("GROUP_DESCRIPTION", group?.description)
                    }
                    startActivity(intent)
                }

                profilePicImageView.setOnClickListener {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                }


                addGroupButton.setOnClickListener {
                    val intent = Intent(this, CreateGroup::class.java)
                    startActivity(intent)
                }

                notificationButton.setOnClickListener {
                    val intent = Intent(this, NotificationActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            // For something if the user is not logged in, might not be necessary
        }

    }
}