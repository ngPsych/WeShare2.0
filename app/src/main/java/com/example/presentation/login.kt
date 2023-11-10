package com.example.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare20.R


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // call db check if user exists
            println("$username" )
            println("$password" )

            val signUpLink: TextView = findViewById(R.id.link_signup)
            signUpLink.setOnClickListener {
                val intent = Intent(this, signUp::class.java)
                startActivity(intent)
            }

            // Add your login logic here
        }
    }
}
