package org.example.project.domain.usecase.transaction

import org.example.project.domain.model.Transaction
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.AccountRepository
import org.example.project.domain.repository.TransactionRepository

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        if (transaction.amount <= 0) {
            return Result.failure(IllegalArgumentException("Сумма должна быть больше нуля"))
        }
        val id = transactionRepository.addTransaction(transaction)

        val balanceChange = if (transaction.type == TransactionType.INCOME) {
            transaction.amount
        } else {
            -transaction.amount
        }
        accountRepository.adjustBalance(transaction.accountId, balanceChange)

        return Result.success(id)
    }
}
