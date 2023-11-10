package com.example.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weshare20.R
import android.widget.Button
import android.widget.EditText
class signUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val usernameEditText: EditText = findViewById(R.id.signupUsername)
        val emailEditText: EditText = findViewById(R.id.signupEmail)
        val passwordEditText: EditText = findViewById(R.id.signupPassword)
        val signUpButton: Button = findViewById(R.id.signupButton)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Implement your sign-up logic here
        }
    }
}