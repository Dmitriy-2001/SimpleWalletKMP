package org.example.project.domain.usecase.analytics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.model.CategoryExpenseShare
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.TransactionRepository

class GetExpenseShareUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(startTimestamp: Long, endTimestamp: Long): Flow<List<CategoryExpenseShare>> {
        return transactionRepository.getTransactionsByDateRange(startTimestamp, endTimestamp).map { transactions ->
            val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
            val totalExpense = expenseTransactions.sumOf { it.amount }

            if (totalExpense == 0.0) {
                emptyList()
            } else {
                val groupByCategory = expenseTransactions
                    .filter { it.category != null }
                    .groupBy { it.category!! }

                groupByCategory.map { (category, txList) ->
                    val categoryTotal = txList.sumOf { it.amount }
                    val percentage = ((categoryTotal / totalExpense) * 100).toFloat()
                    CategoryExpenseShare(
                        category = category,
                        totalAmount = categoryTotal,
                        percentage = percentage
                    )
                }.sortedByDescending { it.totalAmount }
            }
        }
    }
}
