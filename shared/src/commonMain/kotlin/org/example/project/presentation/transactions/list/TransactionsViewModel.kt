package org.example.project.presentation.transactions.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.account.GetAccountsUseCase
import org.example.project.domain.usecase.category.GetCategoriesUseCase
import org.example.project.domain.usecase.transaction.DeleteTransactionUseCase
import org.example.project.domain.usecase.transaction.GetTransactionsUseCase

class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getCategoriesUseCase().collect {
                // Seeding trigger
            }
        }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getTransactionsUseCase(),
                getAccountsUseCase()
            ) { transactions, accounts ->
                val mainAccount = accounts.firstOrNull()
                _state.value.copy(
                    isLoading = false,
                    transactions = transactions,
                    account = mainAccount
                )
            }.catch { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }.collect { updatedState ->
                _state.value = updatedState
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            deleteTransactionUseCase(id)
        }
    }
}
