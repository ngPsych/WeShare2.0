package com.example.test.business

class User() {
    var fullName: String = ""
    var userName: String = ""
    var password: String = ""
    var email: String = ""

    // Constructor
    constructor(fullName: String, userName: String, password: String, email: String) : this() {
        this.fullName = fullName
        this.userName = userName
        this.password = password
        this.email = email
    }

}