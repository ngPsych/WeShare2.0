package com.example.weshare20.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Expense
import com.example.weshare20.business.Group
import com.example.weshare20.business.SessionManager
import com.example.weshare20.business.User
import java.util.UUID

class MyGroups(val participants: List<User>) : AppCompatActivity() {
    private lateinit var db: DatabaseHandler
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_groups)

        db = DatabaseHandler(this)
        sessionManager = SessionManager(this)

        val currentUserId = sessionManager.getUserId()
        if (currentUserId != -1) {
            displayUserGroups(currentUserId)
        }
        /*
        val buttonAddExpense: Button = findViewById(R.id.buttonAddExpense)
        buttonAddExpense.setOnClickListener {
            showAddExpenseDialog()
        }

         */

        // Load and display the list of groups...
    }

    private fun displayUserGroups(userId: Int) {
        val userGroups = db.getUserGroups(userId)

        val listView = findViewById<ListView>(R.id.groupsListView)
        val adapter = GroupAdapter(this, userGroups) { group ->
            showAddExpenseDialog(group)
        }

        listView.adapter = adapter
    }

    private fun showAddExpenseDialog(selectedGroup: Group) {

        // Define payer and groupId here. These should be determined based on your app's flow.
        // For demonstration, I am setting them to default values. Replace with your logic.
        val payer = db.getDefaultPayer() ?: return // Get default payer
        val groupId = db.getCurrentGroupId() ?: return // Get current group ID


        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null)
        val editTextAmount = dialogView.findViewById<EditText>(R.id.editTextAmount)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        // Initialize other input fields similarly

        val editTextPayer = dialogView.findViewById<EditText>(R.id.editTextPayer)
        val editTextGroupId = dialogView.findViewById<EditText>(R.id.editTextGroupId)
        val editTextDate = dialogView.findViewById<EditText>(R.id.editTextDate)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Expense")
            .setView(dialogView)
            .setCancelable(true)
            .show()

        dialogView.findViewById<Button>(R.id.buttonSaveExpense).setOnClickListener {
            val amount = editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
            val description = editTextDescription.text.toString()

            // Retrieve data from other fields
            val payerName = editTextPayer.text.toString()
            val groupIdInput = editTextGroupId.text.toString().toIntOrNull()
            val date = editTextDate.text.toString()
            // val payer = db.getUserByUsername(payerName)
            val payer = db.getUserByUsername(payerName) ?: return@setOnClickListener


            if (amount <= 0 || description.isBlank() || payerName.isBlank() || groupIdInput == null || date.isBlank()) {
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val expense = Expense(
                id = UUID.randomUUID().toString(),
                amount = amount,
                description = description,
                payer = payer,
                groupId = groupIdInput.toString(),
                date = date,
                participants = null
            )

            db.createExpense(expense)
            dialog.dismiss()

            /*else {
                // Handle invalid input
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }

             */


        }
    }
}






