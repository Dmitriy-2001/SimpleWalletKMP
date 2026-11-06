package org.example.project.data.mapper

import org.example.project.data.database.entity.TransactionEntity
import org.example.project.domain.model.Category
import org.example.project.domain.model.Currency
import org.example.project.domain.model.Transaction
import org.example.project.domain.model.TransactionType

fun TransactionEntity.toDomain(category: Category?): Transaction = Transaction(
    id = id,
    amount = amount,
    currency = Currency.fromCode(currency),
    category = category,
    type = try { TransactionType.valueOf(type) } catch (e: Exception) { TransactionType.EXPENSE },
    dateTimestamp = dateTimestamp,
    note = note,
    accountId = accountId
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    amount = amount,
    currency = currency.code,
    categoryId = category?.id ?: 0,
    type = type.name,
    dateTimestamp = dateTimestamp,
    note = note,
    accountId = accountId
)
