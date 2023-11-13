package com.example.weshare20.business

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, "WeShare2.0_Database", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTableQuery = """
            CREATE TABLE Users (
                userID INTEGER PRIMARY KEY AUTOINCREMENT,
                fullname TEXT,
                username TEXT,
                password TEXT,
                phoneNumber INTEGER,
                email TEXT
            );
        """

        db?.execSQL(createUsersTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // You might want to handle database schema upgrades here
    }

    fun createUser(user: User) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put("fullname", user.fullname)
                put("username", user.username)
                put("password", user.password)
                put("phoneNumber", user.phoneNumber)
                put("email", user.email)
            }

            db.insert("Users", null, values)
        }
    }

    @SuppressLint("Range")
    fun authenticateUser(username: String, password: String): Int? {
        return readableDatabase.use { db ->
            var userId: Int? = null

            val query = "SELECT userID FROM Users WHERE username = ? AND password = ?"
            val cursor = db.rawQuery(query, arrayOf(username, password))

            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex("userID"))
            }

            cursor.close()
            userId
        }
    }

    @SuppressLint("Range")
    fun getUserInfo(userId: Int): User? {
        return readableDatabase.use { db ->
            var user: User? = null

            val query = "SELECT * FROM Users WHERE userID = ?"
            val cursor = db.rawQuery(query, arrayOf(userId.toString()))

            if (cursor.moveToFirst()) {
                user = getUserFromCursor(cursor)
            }

            cursor.close()
            user
        }
    }

    @SuppressLint("Range")
    fun getUsernameInfo(username: String): User? {
        return readableDatabase.use { db ->
            var user: User? = null

            val query = "SELECT * FROM Users WHERE username = ?"
            val cursor = db.rawQuery(query, arrayOf(username.toString()))

            if (cursor.moveToFirst()) {
                user = getUserFromCursor(cursor)
            }

            cursor.close()
            user
        }
    }

    fun isUsernameExists(username: String): Boolean {
        return readableDatabase.use { db ->
            var isExists = false

            val query = "SELECT * FROM Users WHERE username = ?"
            val cursor = db.rawQuery(query, arrayOf(username))

            if (cursor.moveToFirst()) {
                isExists = true
            }

            cursor.close()
            isExists
        }
    }

    fun isEmailExist(email: String): Boolean {
        return readableDatabase.use { db ->
            var isExists = false

            val query = "SELECT * FROM Users WHERE email = ?"
            val cursor = db.rawQuery(query, arrayOf(email))

            if (cursor.moveToFirst()) {
                isExists = true
            }

            cursor.close()
            isExists
        }
    }

    @SuppressLint("Range")
    private fun getUserFromCursor(cursor: Cursor): User {
        val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
        val username = cursor.getString(cursor.getColumnIndex("username"))
        val password = cursor.getString(cursor.getColumnIndex("password"))
        val phoneNumber = cursor.getInt(cursor.getColumnIndex("phoneNumber"))
        val email = cursor.getString(cursor.getColumnIndex("email"))

        return User(fullname, username, password, phoneNumber, email)
    }
}
