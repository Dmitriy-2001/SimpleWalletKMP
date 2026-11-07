package org.example.project.presentation.transactions.list

import org.example.project.domain.model.Account
import org.example.project.domain.model.Transaction

data class TransactionsState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val account: Account? = null,
    val searchQuery: String = "",
    val error: String? = null
)
