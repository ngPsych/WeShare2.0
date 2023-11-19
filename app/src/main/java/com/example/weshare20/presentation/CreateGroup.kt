package com.example.weshare20.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Group


class CreateGroup : AppCompatActivity() {

    private var selectedUsernames: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val db = DatabaseHandler(this)
        val usernames = db.getAllUsernames() // Get all usernames from the database


        val editTextGroupName: EditText = findViewById(R.id.editTextGroupName)
        val editTextGroupDescription: EditText = findViewById(R.id.editTextGroupDescription)
        val buttonCreateGroup: Button = findViewById(R.id.buttonSubmitCreateGroup)
        val buttonSelectUsers: Button = findViewById(R.id.buttonSelectUsers) // Button to open user selection dialog

        buttonSelectUsers.setOnClickListener {
            showUserSelectionDialog(usernames)
        }
        buttonCreateGroup.setOnClickListener {
            createGroup(editTextGroupName.text.toString(), editTextGroupDescription.text.toString())
        }
    }

    private fun createGroup(groupName: String, groupDescription: String) {
        val db = DatabaseHandler(this)
        val newGroup = Group(0, groupName, groupDescription, mutableListOf(), mutableListOf())

        // Save the new group and get its ID
        val newGroupId = db.createGroup(newGroup)

        // Convert selected usernames to user IDs
        val userIdsToAdd = selectedUsernames.mapNotNull { db.getUserIdByUsername(it) }

        // Add selected users to the group
        userIdsToAdd.forEach { userId ->
            db.addUserToGroup(userId, newGroupId.toInt())
        }

        // After creating the group and adding users, navigate back or to another activity
        finish() // or start another activity
    }

    private fun showUserSelectionDialog(usernames: List<String>) {
        val checkedItems = BooleanArray(usernames.size)
        val selectedItems = mutableListOf<String>()

        AlertDialog.Builder(this)
            .setTitle("Select Users")
            .setMultiChoiceItems(usernames.toTypedArray(), checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(usernames[which])
                } else {
                    selectedItems.remove(usernames[which])
                }
            }
            .setPositiveButton("OK") { _, _ ->
                selectedUsernames = selectedItems
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}