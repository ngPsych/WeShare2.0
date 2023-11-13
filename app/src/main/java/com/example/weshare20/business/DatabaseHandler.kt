package com.example.weshare20.business

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.weshare20.business.User

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, "WeShare2.0_Database", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTableQuery = """
            CREATE TABLE Users (
                userID INTEGER PRIMARY KEY AUTOINCREMENT,
                fullname TEXT,
                username TEXT,
                password TEXT,
                email TEXT
            );
        """

        db?.execSQL(createUsersTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // drops the table if it already exists? might not be necessary
        //db?.execSQL("DROP TABLE IF EXISTS Users")
        //onCreate(db)
    }

    fun createUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("fullname", user.fullname)
            put("username", user.username)
            put("password", user.password)
            put("email", user.email)
        }

        db?.insert("Users", null, values)

        db?.close()
    }

    @SuppressLint("Range")
    fun authenticateUser(username: String, password: String): Int? {
        val db = this.readableDatabase
        var userId: Int? = null

        val query = "SELECT userID FROM Users WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("userID"))
        }

        cursor.close()
        db.close()

        return userId
    }

    @SuppressLint("Range")
    fun getUserInfo(userId: Int): User? {
        val db = this.readableDatabase
        var user: User? = null

        val query = "SELECT * FROM Users WHERE userID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val email = cursor.getString(cursor.getColumnIndex("email"))

            user = User(fullname, username, password, email)
        }

        cursor.close()
        db.close()

        return user
    }


}