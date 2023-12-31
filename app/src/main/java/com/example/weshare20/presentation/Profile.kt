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

        val profileFullName: TextView = findViewById(R.id.profileFullName)
        val profileNotificationSwitch: Switch = findViewById(R.id.profileNotificationSwitch)
        val profilePhoneNumber: EditText = findViewById(R.id.profilePhoneNumber)
        val profileEmail: EditText = findViewById(R.id.profileEmail)
        val profilePassword: EditText = findViewById(R.id.profilePassword)
        val profileConfirmPassword: EditText = findViewById(R.id.profileConfirmPassword)
        val profileUpdateButton: Button = findViewById(R.id.profileUpdateButton)

        if (userId != -1) {
            val user = db.getUserInfo(userId)

            if (user != null) {
                val fullName = user.fullname
                val phoneNumber = user.phoneNumber
                val email = user.email

                profileFullName.text = fullName
                profilePhoneNumber.hint = phoneNumber.toString()
                profileEmail.hint = email

                profileUpdateButton.setOnClickListener {

                    if (profilePhoneNumber.text.isNotEmpty()) {
                        val newPhoneNumber = profilePhoneNumber.text.toString().toInt()
                        if (user.phoneNumber != newPhoneNumber && !db.isPhoneNumberExist(newPhoneNumber)) {
                            db.updateUserPhoneNumber(userId, newPhoneNumber)
                            Toast.makeText(this, "Phone number updated.", Toast.LENGTH_SHORT).show()
                        } else if (db.isPhoneNumberExist(newPhoneNumber)) {
                            Toast.makeText(this, "PHONE NUMBER ALREADY EXISTS", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        println("PHONE NUMBER: Empty")
                    }

                    // Check and update email
                    val newEmail = profileEmail.text.toString()
                    if (profileEmail.text.isNotEmpty()) {
                        if (user.email != newEmail && !db.isEmailExist(newEmail)) {
                            db.updateUserEmail(userId, newEmail)
                            Toast.makeText(this, "Email updated.", Toast.LENGTH_SHORT).show()
                        } else if (db.isEmailExist(newEmail)) {
                            Toast.makeText(this, "E-MAIL ALREADY EXISTS", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        println("EMAIL: Empty")
                    }

                    val newPassword = profilePassword.text.toString()
                    val confirmPassword = profileConfirmPassword.text.toString()
                    if (newPassword.isNotEmpty()) {
                        if (newPassword == confirmPassword) {
                            db.updateUserPassword(userId, newPassword)
                            Toast.makeText(this, "Password updated.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        println("PASSWORD: Empty")
                    }
                }


                profileNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        Toast.makeText(this, "NOTIFICATION ON", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "NOTIFICATION OFF", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }



    }
}