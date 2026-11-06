package org.example.project.domain.usecase.account

import org.example.project.domain.model.Account
import org.example.project.domain.repository.AccountRepository

class AddAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account): Result<Long> {
        if (account.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Название счета не может быть пустым"))
        }
        val id = accountRepository.addAccount(account)
        return Result.success(id)
    }
}
