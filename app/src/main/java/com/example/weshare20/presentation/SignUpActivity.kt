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
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)

        btnSignUp.setOnClickListener {
            // Logic for inserting into database
            val db = DatabaseHandler(this)

            val fullName = editTextFullname.text.toString()
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString()

            db.createUser(User(fullName, username, password, email))

            Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}