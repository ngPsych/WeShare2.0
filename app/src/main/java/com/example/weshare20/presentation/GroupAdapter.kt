package com.example.weshare20.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.weshare20.R
import com.example.weshare20.business.Group

class GroupAdapter(
    private val context: Context,
    private val groups: List<Group>,
    private val onAddExpenseClick: (Group) -> Unit
) : ArrayAdapter<Group>(context, 0, groups) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_list_group_button, parent, false)

        val group = groups[position]
        val textViewGroupName = layout.findViewById<TextView>(R.id.textViewGroupName)
        val buttonAddExpense = layout.findViewById<Button>(R.id.buttonAddExpense)

        textViewGroupName.text = group.name
        buttonAddExpense.setOnClickListener { onAddExpenseClick(group) }

        return layout
    }
}