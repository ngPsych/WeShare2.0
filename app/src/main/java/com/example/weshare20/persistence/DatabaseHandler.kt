package com.example.weshare20.persistence

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.weshare20.business.User

val DATABASE_NAME = "WeShare2.0"
val TABLE_NAME =  "Users"
val COL_USERID = "userId"
val COL_FULLNAME = "fullname"
val COL_USERNAME = "username"
val COL_PASSWORD = "password"
val COL_EMAIL = "email"

class DatabaseHandler(var context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FULLNAME + " VARCHAR(256)," +
                COL_USERNAME + " VARCHAR(256)," +
                COL_PASSWORD + " VARCHAR(256)," +
                COL_EMAIL + " VARCHAR(256),"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertUser(user : User) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_FULLNAME, user.fullName)
        cv.put(COL_USERNAME, user.userName)
        cv.put(COL_PASSWORD, user.password)
        cv.put(COL_EMAIL, user.email)

        var result = db.insert(TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
}