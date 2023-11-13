package com.example.weshare20.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.SessionManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.loginButton)
        val db = DatabaseHandler(this)
        val session = SessionManager(this)

        db.deleteDatabase(this)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            println("$username" )
            println("$password" )
            // Add your login logic here
            val userId = db.authenticateUser(username, password)

            if (userId != null) {
                // Authentication successful
                session.saveUserId(userId)

                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            } else {
                // Authentication failed
                Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_LONG).show()
            }

        }

        val signupButton = findViewById<Button>(R.id.signupButton)

        signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

}
