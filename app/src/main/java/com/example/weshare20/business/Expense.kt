package com.example.weshare20.business

data class Expense (

    val id: String,
    val amount: Double,
    val description: String,
    val payer: User,
    val groupId: String,
    val date: String, // Consider using a Date type
    val participants: List<User> = emptyList()

)