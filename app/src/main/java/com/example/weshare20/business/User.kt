package com.example.weshare20.business;
class User() {
    var userId: Int = 0
    var fullName: String = ""
    var userName: String = ""
    var password: String = ""
    var email: String = ""

    // Constructor
    constructor(userId: Int, fullName: String, userName: String, password: String, email: String) : this() {
        this.userId = userId
        this.fullName = fullName
        this.userName = userName
        this.password = password
        this.email = email
    }

    // Getters
    fun getUserId(): Int {
        return userId
    }

    fun getFullName(): String {
        return fullName
    }

    fun getUserName(): String {
        return userName
    }

    fun getPassword(): String {
        return password
    }

    fun getEmail(): String {
        return email
    }

    // Setters
    fun setUserId(userId: Int) {
        this.userId = userId
    }

    fun setFullName(fullName: String) {
        this.fullName = fullName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setEmail(email: String) {
        this.email = email
    }
}
