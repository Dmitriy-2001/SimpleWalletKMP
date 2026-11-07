package org.example.project.presentation.transactions.add_edit

import org.example.project.domain.model.Category
import org.example.project.domain.model.Currency
import org.example.project.domain.model.TransactionType

data class AddEditTransactionState(
    val amount: String = "",
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val selectedCurrency: Currency = Currency.KZT,
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val dateTimestamp: Long = 1716000000000L,
    val note: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
