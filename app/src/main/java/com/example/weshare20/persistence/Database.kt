package com.example.weshare20.persistence

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Database {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val databaseConnector = Database()
            val connection = databaseConnector.connect()

            connection?.let {
       /*
                // test om DB virker seperat
                databaseConnector.insertUser(2,"huisdf", "hfisfs",
                    "harun@gmail.com", "gasgdsdfg")

*/
                it.close()
            }
        }
    }
    val jdbcUrl = "jdbc:postgresql://trumpet.db.elephantsql.com:5432/kjcoebww"
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

    fun insertUser(id: Int, fullname: String, username: String, email: String, password: String) {
        val sql = "INSERT INTO users (userid,fullname, username, email, password) VALUES (?, ?, ?, ?, ?)"

        connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1,id)
                stmt.setString(2, fullname)
                stmt.setString(3, username)
                stmt.setString(4, email)
                stmt.setString(5, password)
                stmt.executeUpdate()
            }
        }
    }

}