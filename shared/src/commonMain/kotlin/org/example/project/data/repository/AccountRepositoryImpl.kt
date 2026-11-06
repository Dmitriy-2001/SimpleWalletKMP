package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.database.dao.AccountDao
import org.example.project.data.mapper.toDomain
import org.example.project.data.mapper.toEntity
import org.example.project.domain.model.Account
import org.example.project.domain.repository.AccountRepository

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(id: Long): Account? {
        return accountDao.getAccountById(id)?.toDomain()
    }

    override suspend fun addAccount(account: Account): Long {
        return accountDao.insertAccount(account.toEntity())
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account.toEntity())
    }

    override suspend fun adjustBalance(accountId: Long, amountChange: Double) {
        accountDao.updateBalance(accountId, amountChange)
    }
}
