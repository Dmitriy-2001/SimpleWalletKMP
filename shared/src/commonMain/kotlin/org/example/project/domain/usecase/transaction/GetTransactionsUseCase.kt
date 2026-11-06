package org.example.project.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Transaction
import org.example.project.domain.repository.TransactionRepository

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return transactionRepository.getAllTransactions()
    }
}
