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

        val createNotificationsTableQuery = """
            CREATE TABLE Notifications (
                notificationID INTEGER PRIMARY KEY AUTOINCREMENT,
                userID INTEGER,
                groupID INTEGER,
                message TEXT,
                FOREIGN KEY(userID) REFERENCES Users(userID),
                FOREIGN KEY(groupID) REFERENCES Groups(groupID)
            );
        """

        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createGroupsTableQuery)
        db?.execSQL(createUserGroupTableQuery)
        db?.execSQL(createNotificationsTableQuery)
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
    fun createGroup(group: Group) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("groupName", group.name)
            put("description", group.description)
        }

        db?.insert("Groups", null, values)
        //db?.close()
    }

    fun addUserToGroup(userGroup: UserGroup) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("userID", userGroup.userID)
            put("groupID", userGroup.groupID)
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
                //val groupID = cursor.getInt(cursor.getColumnIndex("groupID"))
                val groupName = cursor.getString(cursor.getColumnIndex("groupName"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                groups.add(Group(groupName, description))
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

    fun getCurrentGroupID(groupName: String, description: String): Int? {
        val db = this.readableDatabase
        val query = """
        SELECT groupID
        FROM Groups
        WHERE groupName = ? AND description = ?
    """
        db.rawQuery(query, arrayOf(groupName, description)).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow("groupID"))
            }
        }
        return null // Return null if no group is found
    }

    fun sendGroupInviteNotification(userId: Int?, groupId: Int?, message: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("userID", userId)
            put("groupID", groupId)
            put("message", message)
            put("status", "PENDING")
        }

        db.insert("Notifications", null, values)
    }

    fun getUserIDByPhoneNumber(phoneNumber: Int): Int? {
        val db = this.writableDatabase
        val query = """
            SELECT userID
            FROM Users
            WHERE phoneNumber = ?
        """

        db.rawQuery(query, arrayOf(phoneNumber.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow("userID"))
            }

        }
        return null
    }

    @SuppressLint("Range")
    fun getUserNotifications(userID: Int): List<Notification> {
        val notifications = mutableListOf<Notification>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM Notifications WHERE userID = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(userID.toString()))

        if (cursor.moveToFirst()) {
            do {
                val groupId = cursor.getInt(cursor.getColumnIndex("groupID"))
                val message = cursor.getString(cursor.getColumnIndex("message"))
                notifications.add(Notification(userID, groupId, message))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return notifications
    }

    fun deleteNotification(userID: Int?, groupID: Int?, message: String?) {
        val db = this.writableDatabase
        db?.delete("Notification", "userID = ? AND groupID = ? AND message = ?", arrayOf(userID.toString(), groupID.toString(), message.toString()))
        //db?.close()
    }

    /* Example of handling an accepted invite
    fun onInviteAccepted(notification: Notification) {
        addUserToGroup(UserGroup(notification.userID, notification.groupID))
        updateNotificationStatus(notification.notificationID, "ACCEPTED")
    }
     */

    /* NOTE
    Nu har vi funktion til at tilføje person i gruppe og notification. Vi skal somehow gøre sådan at
    når man inviterer en person til en gruppe, så sender den notifikationen først, og så når status
    bliver til "ACCEPTED", altså at personen har accepteret, så skal den opdatere og tilføje
    personen til gruppen.

     */
    fun deleteDatabase(context: Context) {
        context.deleteDatabase("WeShare2.0_Database")
    }

}