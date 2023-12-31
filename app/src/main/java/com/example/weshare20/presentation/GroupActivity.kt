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
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Expense
import com.example.weshare20.business.SessionManager
import com.example.weshare20.business.User
import com.example.weshare20.business.UserGroup

class GroupActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHandler
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        db = DatabaseHandler(this)
        session = SessionManager(this)

        val groupName = intent.getStringExtra("GROUP_NAME")
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION")
        val groupNameTextView: TextView = findViewById(R.id.groupNameTextView)
        groupNameTextView.text = groupName
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        descriptionTextView.text = groupDescription

        val nameTextView: TextView = findViewById(R.id.nameTextView)
        nameTextView.text = db.getUserInfo(session.getUserId())?.fullname

        val receiveAmount = db.getTotalAmountToBeReceivedByUserInGroup(
            session.getUserId(),
            db.getCurrentGroupID(groupName.toString(), groupDescription.toString()).toString()
        ) ?: 0.0 // If null, default to 0.0

        val debtAmount = db.getTotalAmountPaidByUserInGroup(
            session.getUserId(),
            db.getCurrentGroupID(groupName.toString(), groupDescription.toString()).toString()
        ) ?: 0.0 // If null, default to 0.0

        val receiveTextView: TextView = findViewById(R.id.receiveTextView)
        receiveTextView.text = receiveAmount.toString()

        val debtTextView: TextView = findViewById(R.id.debtTextView)
        debtTextView.text = debtAmount.toString()

        val backButton: Button = findViewById(R.id.backToHomeButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val addUserButton: Button = findViewById(R.id.addUserButton)
        val editTextViewPhoneNumber: EditText = findViewById(R.id.editTextViewPhoneNumber)
        addUserButton.setOnClickListener {
            if (editTextViewPhoneNumber.text.isNotEmpty()) {
                val phoneNumber = editTextViewPhoneNumber.text.toString().toInt()
                val userID = db.getUserIDByPhoneNumber(phoneNumber)
                val groupID = db.getCurrentGroupID(groupName.toString(), groupDescription.toString())


                val userGroup = UserGroup(userID, groupID)
                db.addUserToGroup(userGroup)

                // Show confirmation
                Toast.makeText(this, "User added to Group", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please enter a Phone Number to add user!", Toast.LENGTH_LONG).show()
            }
        }


        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)
        addExpenseButton.setOnClickListener {
            showAddExpenseDialog()
        }
    }

    private fun showAddExpenseDialog() {


        val groupName = intent.getStringExtra("GROUP_NAME")
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION")
        val groupID = db.getCurrentGroupID(groupName.toString(), groupDescription.toString())
        Toast.makeText(this, "GroupID: $groupID", Toast.LENGTH_LONG).show()


        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null)
        val editTextAmount = dialogView.findViewById<EditText>(R.id.editTextAmount)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)

        val payerSpinner = dialogView.findViewById<Spinner>(R.id.payerSpinner)
        val editTextDate = dialogView.findViewById<EditText>(R.id.editTextDate)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Expense")
            .setView(dialogView)
            .setCancelable(true)
            .show()

        val userIDs = db.getAllUsersInGroup(groupID)
        val userList = mutableListOf<User>()

        for (userID in userIDs) {
            val userInfo = db.getUserInfo(userID)
            userInfo?.let {
                userList.add(it)
            }
        }

        val fullNameToUserMap = userList.associateBy { it.fullname }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fullNameToUserMap.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        payerSpinner.adapter = adapter

        var payer: User? = null

        payerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFullName = parent?.getItemAtPosition(position) as? String
                payer = selectedFullName?.let { fullNameToUserMap[it] }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        dialogView.findViewById<Button>(R.id.buttonSaveExpense).setOnClickListener {
            val amount = editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
            val description = editTextDescription.text.toString()


            val date = editTextDate.text.toString()

            if (amount <= 0 || description.isBlank() || date.isBlank()) {
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = payer?.let {
                if (it.userID != null) {
                    Expense(
                        amount = amount,
                        description = description,
                        payer = it,
                        receiver = session.getUserId(),
                        groupId = groupID.toString(),
                        date = date
                    )
                } else {
                    null
                }
            }

            if (expense != null) {
                db.sendExpenseNotification(expense)
                db.createExpense(expense)
            }
            dialog.dismiss()
        }

    }
}