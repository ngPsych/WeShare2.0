package com.example.weshare20.business

data class Group(
    val id: Int,
    var name: String,
    var description: String,
    val expenses: MutableList<Expense> = mutableListOf(), // This should be a list of Expense objects
    val participants: MutableList<User> = mutableListOf() // Moved this inside the primary constructor
)
