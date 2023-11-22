package com.example.weshare20.business

data class Group(
    var name: String,
    var description: String) {
    val participants: MutableList<String> = mutableListOf()

    fun addParticipant(userId: String) {
        participants.add(userId)
    }
}

