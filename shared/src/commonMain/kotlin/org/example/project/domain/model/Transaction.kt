package org.example.project.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val currency: Currency,
    val category: Category?,
    val type: TransactionType,
    val dateTimestamp: Long,
    val note: String = "",
    val accountId: Long = 1
)
