package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Account

interface AccountRepository {
    fun getAllAccounts(): Flow<List<Account>>
    suspend fun getAccountById(id: Long): Account?
    suspend fun addAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun adjustBalance(accountId: Long, amountChange: Double)
}
