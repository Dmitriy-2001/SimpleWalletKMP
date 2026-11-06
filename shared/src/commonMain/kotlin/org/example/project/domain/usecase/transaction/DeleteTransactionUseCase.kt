package org.example.project.domain.usecase.transaction

import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.AccountRepository
import org.example.project.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transactionId: Long): Result<Unit> {
        val transaction = transactionRepository.getTransactionById(transactionId)
            ?: return Result.failure(IllegalArgumentException("Транзакция не найдена"))

        transactionRepository.deleteTransaction(transactionId)

        val balanceReversal = if (transaction.type == TransactionType.INCOME) {
            -transaction.amount
        } else {
            transaction.amount
        }
        accountRepository.adjustBalance(transaction.accountId, balanceReversal)

        return Result.success(Unit)
    }
}
