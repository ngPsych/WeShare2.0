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

        btnSignUp.setOnClickListener {
            val fullName = editTextFullname.text.toString().trim()
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val phoneNumber = editTextPhoneNumber.text.toString().trim()
            val email = editTextEmail.text.toString().trim()

            if (!validateInputs(fullName, username, password, phoneNumber, email)) {
                return@setOnClickListener
            }

            val usernameResult = db.isUsernameExists(username)
            val emailResult = db.isEmailExist(email)

            when {
                usernameResult -> Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show()
                emailResult -> Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show()
                else -> {
                    db.createUser(User(fullname = fullName, username = username, password = password, phoneNumber = phoneNumber.toInt(), email = email))
                    Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }
    }

    private fun validateInputs(fullName: String, username: String, password: String, phoneNumber: String, email: String): Boolean {
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {

            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_LONG).show()
            return false
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show()
            return false
        }

        if (username.length < 5) {
            Toast.makeText(this, "Username must be at least 5 characters", Toast.LENGTH_LONG).show()
            return false
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone number is missing!", Toast.LENGTH_LONG).show()
            return false
        }

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name is missing!", Toast.LENGTH_LONG).show()
            return false
        }



        return true
    }
}
