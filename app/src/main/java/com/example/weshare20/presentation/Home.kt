package com.example.weshare20.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.R
import com.example.weshare20.business.SessionManager

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val session = SessionManager(this)
        val db = DatabaseHandler(this)

        val userId = session.getUserId()

        if (userId != -1) {
            // User is logged in
            val user = db.getUserInfo(userId)

            if (user != null) {
                val fullname = user.fullname
                val username = user.username
                val email = user.email

                Toast.makeText(this, "Hi $fullname, $username, $email", Toast.LENGTH_LONG).show()
                // Now you have the user's information
                // You can use fullname, username, and email as needed
            }
        } else {
            // For something if the user is not logged in, might not be necessary
        }
    }
}