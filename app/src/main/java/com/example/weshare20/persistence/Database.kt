package com.example.weshare20.persistence

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class Database {
    private val url = "jdbc:postgresql://trumpet.db.elephantsql.com/kjcoebww"
    private val username = "kjcoebww"
    private val password = "9UjWwwxCx2jwqrX2RI0Ga2bOygVawfCc"

    private fun connect(): Connection? {
        try {
            Class.forName("org.postgresql.Driver")
            return DriverManager.getConnection(url, username, password)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun insertUser(userId: Int, fullName: String, username: String, password: String, email: String): Boolean {
        val connection = connect()
        if (connection != null) {
            try {
                val query = "INSERT INTO users (user_id, fullname, username, password, email) VALUES (?, ?, ?, ?, ?)"
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, userId)
                preparedStatement.setString(2, fullName)
                preparedStatement.setString(3, username)
                preparedStatement.setString(4, password)
                preparedStatement.setString(5, email)

                val rowsInserted = preparedStatement.executeUpdate()
                return rowsInserted > 0
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                connection.close()
            }
        }
        return false
    }
}
