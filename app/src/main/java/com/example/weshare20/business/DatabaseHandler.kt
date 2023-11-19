package com.example.weshare20.business

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, "WeShare2.0_Database", null, 1) {
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
        //EXPENSEE
        val createExpensesTableQuery = """
    CREATE TABLE Expenses (
        expenseId INTEGER PRIMARY KEY AUTOINCREMENT,
        amount REAL,
        description TEXT,
        payerId INTEGER,
        groupId INTEGER,
        date TEXT,
        FOREIGN KEY(payerId) REFERENCES Users(userID),
        FOREIGN KEY(groupId) REFERENCES Groups(groupId)
    );
"""

        db?.execSQL(createExpensesTableQuery)

        val createGroupsTableQuery = """
    CREATE TABLE Groups (
        groupID INTEGER PRIMARY KEY AUTOINCREMENT,
        groupName TEXT,
        description TEXT
    );
"""

        val createUserGroupTableQuery = """
    CREATE TABLE UserGroups (
        userID INTEGER,
        groupID INTEGER,
        FOREIGN KEY(userID) REFERENCES Users(userID),
        FOREIGN KEY(groupID) REFERENCES Groups(groupID),
        PRIMARY KEY(userID, groupID)
    );
"""
        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createGroupsTableQuery)
        db?.execSQL(createUserGroupTableQuery)
    }

    //EXPENSEEE

    @SuppressLint("Range")
    fun getUserIdByUsername(username: String): Int? {
        val db = this.readableDatabase
        var userId: Int? = null

        val query = "SELECT userID FROM Users WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("userID"))
        }

        cursor.close()
 //       db.close()

        return userId
    }

    fun getAllUsernames(): List<String> {
        val db = this.readableDatabase
        val usernames = mutableListOf<String>()
        val selectQuery = "SELECT username FROM Users"

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(cursor.getColumnIndex("username"))
                usernames.add(username)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return usernames
    }


    fun createExpense(expense: Expense) {
        val db = this.writableDatabase

        val payerId = getUserIdByUsername(expense.payer.username) // Get userId based on username
        payerId?.let { id ->
            val values = ContentValues().apply {
                put("amount", expense.amount)
                put("description", expense.description)
                put("payerId", id)
                put("groupId", expense.groupId)
                put("date", expense.date)
            }

            db.insert("Expenses", null, values)
        }

     //   db.close()
    }

    @SuppressLint("Range")
    fun getExpensesForGroup(groupId: String): List<Expense> {
        val db = this.readableDatabase
        val expenses = mutableListOf<Expense>()
        val query = "SELECT * FROM Expenses WHERE groupId = ?"
        val cursor = db.rawQuery(query, arrayOf(groupId))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex("expenseId"))
                val amount = cursor.getDouble(cursor.getColumnIndex("amount"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val payerId = cursor.getInt(cursor.getColumnIndex("payerId"))
                val date = cursor.getString(cursor.getColumnIndex("date"))

                // Assuming you have a method to get User by userId
                val payer = getUserInfo(payerId)
                payer?.let {
                    val expense = Expense(id, amount, description, it, groupId, date)
                    expenses.add(expense)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return expenses
    }

    fun splitExpense(expense: Expense, group: Group): Map<User, Double> {
        val amountPerPerson = expense.amount / group.participants.size
        return group.participants.associateWith { amountPerPerson }
    }

    @SuppressLint("Range")
    fun getDefaultPayer(): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM Users LIMIT 1"
        val cursor = db.rawQuery(query, null)
        var user: User? = null

        if (cursor.moveToFirst()) {
            val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val phoneNumber = cursor.getInt(cursor.getColumnIndex("phoneNumber"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            user = User(fullname, username, password, phoneNumber, email)
        }

        cursor.close()
       // db.close()
        return user
    }

    @SuppressLint("Range")
    fun getCurrentGroupId(): Int? {
        val db = this.readableDatabase
        val query = "SELECT groupID FROM Groups LIMIT 1"
        val cursor = db.rawQuery(query, null)
        var groupId: Int? = null

        if (cursor.moveToFirst()) {
            groupId = cursor.getInt(cursor.getColumnIndex("groupID"))
        }

        cursor.close()
       // db.close()
        return groupId
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
            put("phoneNumber", user.phoneNumber)
            put("email", user.email)
        }

        db?.insert("Users", null, values)

        //db?.close()
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
        //db.close()

        return userId
    }

    // This functions gets UserInfo through searching the userID (used for session)
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
            val phoneNumber = cursor.getInt(cursor.getColumnIndex("phoneNumber"))
            val email = cursor.getString(cursor.getColumnIndex("email"))

            user = User(fullname, username, password, phoneNumber, email)
        }

        cursor.close()
        //db.close()

        return user
    }

    // Will be used for later when searching for user through username to add into group
    @SuppressLint("Range")
    fun getUsernameInfo(username: String): User? {
        val db = this.readableDatabase
        var user: User? = null

        val query = "SELECT * FROM Users WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username.toString()))

        if (cursor.moveToFirst()) {
            val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val phoneNumber = cursor.getInt(cursor.getColumnIndex("phoneNumber"))
            val email = cursor.getString(cursor.getColumnIndex("email"))

            user = User(fullname, username, password, phoneNumber, email)
        }

        cursor.close()
        //db.close()

        return user
    }

    @SuppressLint("Range")
    fun getUserByUsername(username: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM Users WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var user: User? = null

        if (cursor.moveToFirst()) {
            val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val phoneNumber = cursor.getInt(cursor.getColumnIndex("phoneNumber"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            user = User(fullname, username, password, phoneNumber, email)
        }

        cursor.close()
        db.close()
        return user
    }


    fun isUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        var isExists = false

        val query = "SELECT * FROM Users WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.moveToFirst()) {
            // Username exists
            isExists = true
        }

        cursor.close()
        //db.close()

        return isExists
    }

    fun isEmailExist(email: String): Boolean {
        val db = this.readableDatabase
        var isExists = false

        val query = "SELECT * FROM Users WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        if (cursor.moveToFirst()) {
            // email exists
            isExists = true
        }

        cursor.close()
        //db.close()

        return isExists
    }

    fun isPhoneNumberExist(phoneNumber: Int): Boolean {
        val db = this.readableDatabase
        var isExists = false

        val query = "SELECT * FROM Users WHERE phoneNumber = ?"
        val cursor = db.rawQuery(query, arrayOf(phoneNumber.toString()))

        if (cursor.moveToFirst()) {
            isExists = true
        }

        cursor.close()
        //db.close()

        return isExists
    }


    // ----- Profile (Update) ----- //
    fun updateUserPhoneNumber(userId: Int, newPhoneNumber: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("phoneNumber", newPhoneNumber)

        db.update("Users", values, "userID = ?", arrayOf(userId.toString()))
        //db.close()
    }

    fun updateUserEmail(userId: Int, newEmail: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("email", newEmail)

        db.update("Users", values, "userID = ?", arrayOf(userId.toString()))
        //db.close()
    }

    fun updateUserPassword(userId: Int, newPassword: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("password", newPassword)

        db.update("Users", values, "userID = ?", arrayOf(userId.toString()))
        //db.close()
    }

    // ----- Group ----- //
    fun createGroup(group: Group): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("groupName", group.name)
            put("description", group.description)
            // Add other fields as necessary
        }

        val groupId = db.insert("Groups", null, values)
        db.close()
        return groupId // This is the ID of the newly created group
    }


    fun addUserToGroup(userID: Int, groupID: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("userID", userID)
            put("groupID", groupID)
        }

        db?.insert("UserGroups", null, values)
        //db?.close()
    }

    fun removeUserFromGroup(userID: Int, groupID: Int) {
        val db = this.writableDatabase
        db?.delete("UserGroups", "userID=? AND groupID=?", arrayOf(userID.toString(), groupID.toString()))
        //db?.close()
    }

    @SuppressLint("Range")
    fun getUserGroups(userID: Int): List<Group> {
        val groups = mutableListOf<Group>()
        val db = this.readableDatabase
        val selectQuery = """
        SELECT g.groupID, g.groupName, g.description 
        FROM Groups g 
        INNER JOIN UserGroups ug ON g.groupID = ug.groupID 
        WHERE ug.userID = ?
    """
        val cursor = db.rawQuery(selectQuery, arrayOf(userID.toString()))

        if (cursor.moveToFirst()) {
            do {
                val groupID = cursor.getInt(cursor.getColumnIndex("groupID"))
                val groupName = cursor.getString(cursor.getColumnIndex("groupName"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                groups.add(Group(groupID, groupName, description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        //db.close()
        return groups
    }

    @SuppressLint("Range")
    fun getGroupUsers(groupID: Int): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val selectQuery = """
        SELECT u.userID, u.fullname, u.username, u.password, u.phoneNumber, u.email 
        FROM Users u 
        INNER JOIN UserGroups ug ON u.userID = ug.userID 
        WHERE ug.groupID = ?
    """
        val cursor = db.rawQuery(selectQuery, arrayOf(groupID.toString()))

        if (cursor.moveToFirst()) {
            do {
                val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
                val username = cursor.getString(cursor.getColumnIndex("username"))
                val password = cursor.getString(cursor.getColumnIndex("password"))
                val phoneNumber = cursor.getInt(cursor.getColumnIndex("phoneNumber"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                users.add(User(fullname, username, password, phoneNumber, email))
            } while (cursor.moveToNext())
        }

        cursor.close()
        //db.close()
        return users
    }

    fun deleteDatabase(context: Context) {
        context.deleteDatabase("WeShare2.0_Database")
    }

}