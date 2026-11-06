package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.example.project.data.database.dao.CategoryDao
import org.example.project.data.database.dao.TransactionDao
import org.example.project.data.mapper.toDomain
import org.example.project.data.mapper.toEntity
import org.example.project.domain.model.Transaction
import org.example.project.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return combine(
            transactionDao.getAllTransactions(),
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            val categoryMap = categories.associateBy { it.id }
            transactions.map { entity ->
                val category = categoryMap[entity.categoryId]?.toDomain()
                entity.toDomain(category)
            }
        }
    }

    override fun getTransactionsByDateRange(startTimestamp: Long, endTimestamp: Long): Flow<List<Transaction>> {
        return combine(
            transactionDao.getTransactionsByDateRange(startTimestamp, endTimestamp),
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            val categoryMap = categories.associateBy { it.id }
            transactions.map { entity ->
                val category = categoryMap[entity.categoryId]?.toDomain()
                entity.toDomain(category)
            }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        val entity = transactionDao.getTransactionById(id) ?: return null
        val category = categoryDao.getCategoryById(entity.categoryId)?.toDomain()
        return entity.toDomain(category)
    }

    override suspend fun addTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransactionById(id)
    }
}
