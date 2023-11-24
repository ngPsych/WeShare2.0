package com.example.weshare20.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Expense
import com.example.weshare20.business.User

class GroupActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        db = DatabaseHandler(this)

        val groupName = intent.getStringExtra("GROUP_NAME")
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION")
        Toast.makeText(this, "Welcome to $groupName - $groupDescription", Toast.LENGTH_LONG).show()

        val backButton: Button = findViewById(R.id.backToHomeButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java) // Replace YourTargetActivity with the actual class name of your target activity
            startActivity(intent)
        }

        val addUserButton: Button = findViewById(R.id.addUserButton)
        val editTextViewPhoneNumber: EditText = findViewById(R.id.editTextViewPhoneNumber)
        addUserButton.setOnClickListener {
            val userID = db.getUserIDByPhoneNumber(editTextViewPhoneNumber.text.toString().toInt())
            val groupID = db.getCurrentGroupID(groupName.toString(), groupDescription.toString())
            db.sendGroupInviteNotification(userID, groupID, "You have been invited to join $groupName")
            Toast.makeText(this, "UserID: $userID, GroupID: $groupID", Toast.LENGTH_LONG).show()
        }

        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)
        addExpenseButton.setOnClickListener {
            showAddExpenseDialog()
        }
    }

    private fun showAddExpenseDialog() {

        // Define payer and groupId here. These should be determined based on your app's flow.
        // For demonstration, I am setting them to default values. Replace with your logic.
        val groupName = intent.getStringExtra("GROUP_NAME")
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION")
        val groupID = db.getCurrentGroupID(groupName.toString(), groupDescription.toString())
        Toast.makeText(this, "GroupID: $groupID", Toast.LENGTH_LONG).show()


        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null)
        val editTextAmount = dialogView.findViewById<EditText>(R.id.editTextAmount)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        // Initialize other input fields similarly

        val payerSpinner = dialogView.findViewById<Spinner>(R.id.payerSpinner)
        val editTextDate = dialogView.findViewById<EditText>(R.id.editTextDate)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Expense")
            .setView(dialogView)
            .setCancelable(true)
            .show()

        val userIDs = db.getAllUsersInGroup(groupID)
        val userList = mutableListOf<User>()
        Toast.makeText(this, "UserID: $userIDs", Toast.LENGTH_LONG).show()

        for (userID in userIDs) {
            val userInfo = db.getUserInfo(userID)
            userInfo?.let {
                userList.add(it)
            }
        }

        val myList = listOf(1, 2, 3, 4, 5) // Creates an immutable list

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, myList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        payerSpinner.adapter = adapter

        var payer: User? = null // Initialize payer as nullable User

        payerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem is User) {
                    payer = selectedItem
                    // Now you have the selected User object in the payer variable
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle no item selected
            }
        }

        dialogView.findViewById<Button>(R.id.buttonSaveExpense).setOnClickListener {
            val amount = editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
            val description = editTextDescription.text.toString()

            // Retrieve data from other fields
            val date = editTextDate.text.toString()

            if (amount <= 0 || description.isBlank() || date.isBlank()) {
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val expense = payer?.let { it1 ->
                Expense(
                    amount = amount,
                    description = description,
                    payer = it1,
                    groupId = groupID.toString(),
                    date = date,
                    participants = null
                )
            }

            if (expense != null) {
                db.createExpense(expense)
            }
            dialog.dismiss()

            /*else {
                // Handle invalid input
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }

             */


        }
    }
}