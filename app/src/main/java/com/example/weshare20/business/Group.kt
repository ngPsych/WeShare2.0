package com.example.weshare20.business

class Group {
    var groupName: String = ""
    var description: String = ""

    constructor(groupName: String, description: String) {
        this.groupName = groupName
        this.description = description
    }

    fun getGroupName(): String {
        return groupName
    }

    fun getDescription(): String {
        return description
    }

    fun setGroupName(groupName: String) {
        this.groupName = groupName
    }

    fun setDescription(description: String) {
        this.description = description
    }

}