package com.example.weshare20.business

class ExpenseCalculator {
    companion object {
        fun calculateOwesOrOwed(group: Group): Map<User, Double> {
            val balanceMap = mutableMapOf<User, Double>()

            // Initialize balances for each participant
            for (participant in group.participants) {
                balanceMap[participant] = 0.0
            }

            // Update balances based on expenses
            for (expense in group.expenses) {
                val payer = expense.payer
                val totalParticipants = expense.participants.size + 1 // Including the payer
                val individualShare = expense.amount / totalParticipants

                // Deduct the individual share from the payer
                balanceMap[payer] = balanceMap.getOrDefault(payer, 0.0) - expense.amount

                // Add the individual share to each participant
                for (participant in expense.participants) {
                    balanceMap[participant] = balanceMap.getOrDefault(participant, 0.0) + individualShare
                }
            }

            return balanceMap
        }
    }
}