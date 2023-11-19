package com.example.weshare20.business

data class Group(
    val id: String,
    var name: String,
    val expenses: MutableList<Expense> = mutableListOf(),

    var description: String

) {
        val participants: MutableList<User> = mutableListOf()
}
