package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.test.business.User
import com.example.test.databinding.ActivityMainBinding
import com.example.test.persistence.DatabaseHandler

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val context = this

        binding.createButton.setOnClickListener {
            if (binding.fullNameText.toString().length > 0 &&
                binding.usernameText.toString().length > 0 &&
                binding.passwordText.toString().length > 0 &&
                binding.emailText.toString().length > 0) {
                var user = User(binding.fullNameText.toString(), binding.usernameText.toString(),
                    binding.passwordText.toString(), binding.emailText.toString())
                var db = DatabaseHandler(context)
                db.insertUser(user)
            } else {
                Toast.makeText(context, "Please fill out all the fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}