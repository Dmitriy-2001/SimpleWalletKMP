package org.example.project.domain.usecase.analytics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.model.Currency
import org.example.project.domain.model.MonthlySummary
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.TransactionRepository

class GetMonthlySummaryUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        startTimestamp: Long,
        endTimestamp: Long,
        defaultCurrency: Currency = Currency.KZT
    ): Flow<MonthlySummary> {
        return transactionRepository.getTransactionsByDateRange(startTimestamp, endTimestamp).map { transactions ->
            var incomeSum = 0.0
            var expenseSum = 0.0

            transactions.forEach { tx ->
                if (tx.type == TransactionType.INCOME) {
                    incomeSum += tx.amount
                } else {
                    expenseSum += tx.amount
                }
            }

            MonthlySummary(
                totalIncome = incomeSum,
                totalExpense = expenseSum,
                netBalance = incomeSum - expenseSum,
                currency = defaultCurrency
            )
        }
    }
}
