class ExpenseManager {
    private val expenses: MutableList<Expense> = mutableListOf()

    fun createExpense(payer: User, amount: Double, group: Group, receipt: String? = null): Expense {
        val newExpense = Expense(generateExpenseId(), payer, amount, group, receipt)
        expenses.add(newExpense)
        return newExpense
    }

    fun splitExpense(expense: Expense) {
        val splitAmount = expense.amount / expense.group.participants.size
        expense.group.participants.forEach { user ->
            // Handle splitting the expense among group members
            // Update each user's balance accordingly
        }
    }

    private fun generateExpenseId(): String {
        // Implement your logic to generate a unique expense ID
        return "expense_${System.currentTimeMillis()}"
    }
}