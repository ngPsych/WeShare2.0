package com.example.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weshare20.R
import android.widget.Button
import android.widget.EditText
import com.example.weshare20.persistence.Database

class signUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val fullnameEditText: EditText = findViewById(R.id.signupFullname)
        val usernameEditText: EditText = findViewById(R.id.signupUsername)
        val emailEditText: EditText = findViewById(R.id.signupEmail)
        val passwordEditText: EditText = findViewById(R.id.signupPassword)
        val signUpButton: Button = findViewById(R.id.signupButton)

        signUpButton.setOnClickListener {
            val fullName = fullnameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Implement your sign-up logic here

// Kalder DB
            val databaseConnector = Database()
            val connection = databaseConnector.connect()

            if (connection != null) {
                // Insert user
                println("Connection established")
                // id skal sendes til DB apparently
                databaseConnector.insertUser(3,fullName, username, email, password)

                // Close the database connection
                connection.close()
            }

        }
    }
}