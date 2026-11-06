package org.example.project.domain.usecase.transaction

import org.example.project.domain.model.Transaction
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.AccountRepository
import org.example.project.domain.repository.TransactionRepository

class EditTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(newTransaction: Transaction): Result<Unit> {
        if (newTransaction.amount <= 0) {
            return Result.failure(IllegalArgumentException("Сумма должна быть больше нуля"))
        }
        val oldTransaction = transactionRepository.getTransactionById(newTransaction.id)
            ?: return Result.failure(IllegalArgumentException("Исходная транзакция не найдена"))

        val oldReversal = if (oldTransaction.type == TransactionType.INCOME) {
            -oldTransaction.amount
        } else {
            oldTransaction.amount
        }

        val newChange = if (newTransaction.type == TransactionType.INCOME) {
            newTransaction.amount
        } else {
            -newTransaction.amount
        }

        transactionRepository.updateTransaction(newTransaction)
        accountRepository.adjustBalance(newTransaction.accountId, oldReversal + newChange)

        return Result.success(Unit)
    }
}
