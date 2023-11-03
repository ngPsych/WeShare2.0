package com.example.weshare20.persistence

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Database {
    fun connect() {
        // Define the database URL, username, and password
        // postgres://kjcoebww:9UjWwwxCx2jwqrX2RI0Ga2bOygVawfCc@trumpet.db.elephantsql.com/kjcoebww
        val url = "jdbc:postgres://trumpet.db.elephantsql.com/kjcoebww"
        val username = "kjcoebww"
        val password = "9UjWwwxCx2jwqrX2RI0Ga2bOygVawfCc"

        // Attempt to establish a database connection
        try {
            // Register the JDBC driver (This step may be optional for some databases)
            Class.forName("org.postgresql.Driver")

            // Open a connection to the database
            val connection: Connection = DriverManager.getConnection(url, username, password)

            // Now you have a connection to the database

            // Perform database operations here

            // Don't forget to close the connection when you're done
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}