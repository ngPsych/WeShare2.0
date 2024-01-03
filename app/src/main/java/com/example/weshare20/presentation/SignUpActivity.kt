package com.example.weshare20.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.User
import com.example.weshare20.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val editTextFullname: EditText = findViewById(R.id.editTextFullname)
        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextPhoneNumber: EditText = findViewById(R.id.editTextPhoneNumber)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)

        val db = DatabaseHandler(this)
        createNotificationChannel()

        btnSignUp.setOnClickListener {
            val fullName = editTextFullname.text.toString()
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString().toInt()
            val email = editTextEmail.text.toString()

            val usernameResult = db.isUsernameExists(username)
            val emailResult = db.isEmailExist(email)

            when {
                usernameResult -> {
                    // Username already exists
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show()
                }
                emailResult -> {
                    // Email already exists
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show()
                }
                else -> {
                    db.createUser(User(fullname = fullName, username = username, password = password, phoneNumber = phoneNumber, email = email))

                    Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

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