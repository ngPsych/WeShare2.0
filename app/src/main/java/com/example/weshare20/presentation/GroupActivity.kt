package com.example.weshare20.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.weshare20.R

class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val groupName = intent.getStringExtra("GROUP_NAME")
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION")
        Toast.makeText(this, "Welcome to $groupName - $groupDescription", Toast.LENGTH_LONG).show()
    }
}