package com.example.weshare20.business

data class Expense(
    val amount: Double,
    val description: String,
    val payer: User,
    val receiver: Int,
    val groupId: String,
    val date: String, // Consider using a Date type
)