package org.example.project.domain.usecase.analytics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.model.DailyBreakdown
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.TransactionRepository

class GetDailyBreakdownUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        startTimestamp: Long,
        endTimestamp: Long,
        daysInMonth: Int = 31
    ): Flow<List<DailyBreakdown>> {
        return transactionRepository.getTransactionsByDateRange(startTimestamp, endTimestamp).map { transactions ->
            val dailyIncomeMap = mutableMapOf<Int, Double>()
            val dailyExpenseMap = mutableMapOf<Int, Double>()

            transactions.forEach { tx ->
                val localDate = Instant.fromEpochMilliseconds(tx.dateTimestamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                val day = localDate.dayOfMonth

                if (tx.type == TransactionType.INCOME) {
                    dailyIncomeMap[day] = (dailyIncomeMap[day] ?: 0.0) + tx.amount
                } else {
                    dailyExpenseMap[day] = (dailyExpenseMap[day] ?: 0.0) + tx.amount
                }
            }

            (1..daysInMonth).map { day ->
                DailyBreakdown(
                    dayOfMonth = day,
                    income = dailyIncomeMap[day] ?: 0.0,
                    expense = dailyExpenseMap[day] ?: 0.0
                )
            }
        }
    }
}
