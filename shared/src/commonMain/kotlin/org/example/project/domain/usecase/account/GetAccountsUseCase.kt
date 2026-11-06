package org.example.project.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Account
import org.example.project.domain.repository.AccountRepository

class GetAccountsUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<List<Account>> {
        return accountRepository.getAllAccounts()
    }
}
