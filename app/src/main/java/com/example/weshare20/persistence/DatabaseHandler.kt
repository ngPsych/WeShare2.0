package com.example.weshare20.persistence

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.weshare20.business.User
import com.example.weshare20.business.Group

val DATABASE_NAME = "WeShare2.0"
val USERS_TABLE_NAME =  "Users"
val COL_USERID = "userId"
val COL_FULLNAME = "fullName"
val COL_USERNAME = "username"
val COL_PASSWORD = "password"
val COL_EMAIL = "email"

val GROUP_TABLE_NAME = "Groups"
val COL_GROUPID = "groupId"
val COL_GROUPNAME = "groupName"
val COL_DESCRIPTION = "description"

val MEMBERSHIP_TABLE_NAME = "Membership"
val COL_MEMBERSHIPID = "membershipId"
val COL_MUSERID = "userId"
val COL_MGROUPID = "groupId"
val COL_ROLE = "role"
val COL_JOINDATE = "joinDate"

class DatabaseHandler(var context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                COL_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FULLNAME + " VARCHAR(256) NOT NULL," +
                COL_USERNAME + " VARCHAR(256) NOT NULL," +
                COL_PASSWORD + " VARCHAR(256) NOT NULL," +
                COL_EMAIL + " VARCHAR(256) NOT NULL)"

        val createGroupTable = "CREATE TABLE " + GROUP_TABLE_NAME + " (" +
                COL_GROUPID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_GROUPNAME + " VARCHAR(256) NOT NULL)" +
                COL_DESCRIPTION + " VARCHAR(256) NOT NULL)"

        val createMembershipTable = "CREATE TABLE " + MEMBERSHIP_TABLE_NAME + " (" +
                COL_MEMBERSHIPID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_MUSERID + " INT NOT NULL," +
                COL_MGROUPID + " INT NOT NULL," +
                COL_ROLE + " VARCHAR(256) NOT NULL," +
                COL_JOINDATE + " VARCHAR(256) NOT NULL," +
                "FOREIGN KEY (" + COL_MUSERID + ") REFERENCES Users(" + COL_MUSERID + ")," +
                "FOREIGN KEY (" + COL_MGROUPID + ") REFERENCES Groups(" + COL_MGROUPID + ")"

        db?.execSQL(createUserTable)
        db?.execSQL(createGroupTable)
        db?.execSQL(createMembershipTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    // Muligvis lav om på Insert funktionerne, så den tager værdierne for name, pw, email osv i parameteren og ikke er indsat direkte i koden
    fun insertUser(user : User) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_FULLNAME, user.fullName)
        cv.put(COL_USERNAME, user.userName)
        cv.put(COL_PASSWORD, user.password)
        cv.put(COL_EMAIL, user.email)

        var result = db.insert(USERS_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertGroup(group : Group) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_GROUPNAME, group.groupName)
        cv.put(COL_DESCRIPTION, group.description)

        var result = db.insert(GROUP_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    // Kig på at lave en funktion for Membership, der skal bare lige brainstormes hvordan den fungerer.
}