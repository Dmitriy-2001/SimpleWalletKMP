package org.example.project.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Transaction
import org.example.project.domain.repository.TransactionRepository

class GetMonthlyTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(startTimestamp: Long, endTimestamp: Long): Flow<List<Transaction>> {
        return transactionRepository.getTransactionsByDateRange(startTimestamp, endTimestamp)
    }
}
