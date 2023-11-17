package com.example.weshare20.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.SessionManager

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val session = SessionManager(this)
        val db = DatabaseHandler(this)
        val userId = session.getUserId()

        val profileUsername: TextView = findViewById(R.id.profileFullName)
        val profileNotificationSwitch: Switch = findViewById(R.id.profileNotificationSwitch)
        val profilePhoneNumber: EditText = findViewById(R.id.profilePhoneNumber)
        val profileEmail: EditText = findViewById(R.id.profileEmail)
        val profilePassword: EditText = findViewById(R.id.profilePassword)
        val profileConfirmPassword: EditText = findViewById(R.id.profileConfirmPassword)
        val profileUpdateButton: Button = findViewById(R.id.profileUpdateButton)

        profilePhoneNumber.hint = "New hint text"

        if (userId != -1) {
            val user = db.getUserInfo(userId)

            if (user != null) {
                val fullName = user.fullname
                val username = user.username
                val email = user.email

                profileUsername.text = fullName
                profilePhoneNumber.hint = username
                profileEmail.hint = email

                profileUpdateButton.setOnClickListener {
                    if (user.phoneNumber != profilePhoneNumber.text.toString().toInt() && !db.isPhoneNumberExist(profilePhoneNumber.text.toString().toInt())) {
                        db.updateUserPhoneNumber(userId, profilePhoneNumber.text.toString().toInt())
                    } else if (db.isPhoneNumberExist(profilePhoneNumber.text.toString().toInt())) {
                        Toast.makeText(this, "PHONE NUMBER ALREADY EXIST", Toast.LENGTH_LONG).show()
                    }

                    if (user.email != profileEmail.text.toString() && !db.isEmailExist(profileEmail.text.toString())) {
                        db.updateUserEmail(userId, profileEmail.text.toString())
                    } else if (db.isEmailExist(profileEmail.text.toString())) {
                        Toast.makeText(this, "E-MAIL ALREADY EXIST", Toast.LENGTH_LONG).show()
                    }

                    if (user.password != profilePassword.text.toString() && profilePassword.text.toString() == profileConfirmPassword.text.toString()) {
                        db.updateUserPassword(userId, profilePassword.text.toString())
                    }
                }

                profileNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // The switch is on
                        Toast.makeText(this, "NOTIFICATION ON", Toast.LENGTH_LONG).show()
                    } else {
                        // The switch is off
                        Toast.makeText(this, "NOTIFICATION OFF", Toast.LENGTH_LONG).show()
                    }
                }

                // If you want to set the switch based on a certain condition:
                profileNotificationSwitch.isChecked = true // or false
            }
        }



    }
}