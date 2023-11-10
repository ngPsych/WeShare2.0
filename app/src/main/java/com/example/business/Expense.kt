data class Expense(val id: String, val payer: User, val amount: Double, val group: Group, val receipt: String? = null)
