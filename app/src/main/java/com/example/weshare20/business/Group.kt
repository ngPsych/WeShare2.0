package com.example.weshare20.business

data class Group(
    val id: String,
    var name: String,
    var description: String) {
        val participants: MutableList<User> = mutableListOf()
}