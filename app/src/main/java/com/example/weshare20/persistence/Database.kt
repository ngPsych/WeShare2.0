package com.example.weshare20.persistence

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Database {
    fun main() {
        val databaseConnector = Database()
        val connection = databaseConnector.connect()

        connection?.let {
            // Perform database operations here
            // ...

            // Don't forget to close the connection
            it.close()
        }
    }
    val jdbcUrl = "jdbc:postgres://trumpet.db.elephantsql.com/kjcoebww"
    val username = "kjcoebww"
    val password = "9UjWwwxCx2jwqrX2RI0Ga2bOygVawfCc"

    fun connect(): Connection? {
        return try {
            DriverManager.getConnection(jdbcUrl, username, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun insertUser(fullName: String, username: String, email: String, password: String) {
        val sql = "INSERT INTO users (fullname, username, email, password) VALUES (?, ?, ?, ?)"

        connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, fullName)
                stmt.setString(2, username)
                stmt.setString(3, email)
                stmt.setString(4, password)
                stmt.executeUpdate()
            }
        }
    }

}