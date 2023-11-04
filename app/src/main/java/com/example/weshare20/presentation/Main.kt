package com.example.weshare20.presentation

import com.example.weshare20.business.User
import com.example.weshare20.persistence.Database

fun main() {
    // Create a new User object and set its properties using the constructor
/*    val user = User(
        userId = 1,
        fullName = "John Doe",
        userName = "johndoe",
        password = "password123",
        email = "johndoe@example.com"
    )

    // You can also update the user's properties using the setters
    user.setFullName("Jane Smith")
    user.setEmail("janesmith@example.com")

    // Accessing user properties
    println("User ID: ${user.getUserId()}")
    println("Full Name: ${user.getFullName()}")
    println("Username: ${user.getUserName()}")
    println("Password: ${user.getPassword()}")
    println("Email: ${user.getEmail()}")*/
    val database = Database()
    val user = User(
        userId = 2,
        fullName = "Alex",
        userName = "alngu18",
        password = "123456",
        email = "alngu18@student.sdu.dk"
    )

    if (database.insertUser(user.getUserId(), user.getFullName(), user.getUserName(), user.getPassword(), user.getEmail())) {
        println("User inserted successfully.")
    } else {
        println("Failed to insert the user.")
    }
}