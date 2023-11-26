package com.example.weshare20.business

data class User(
    val userID: Int? = null, // Make userID nullable and provide a default value of null
    val fullname: String,
    val username: String,
    val password: String,
    val phoneNumber: Int,
    val email: String
)