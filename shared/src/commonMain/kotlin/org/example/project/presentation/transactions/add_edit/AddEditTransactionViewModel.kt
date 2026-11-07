package org.example.project.presentation.transactions.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.model.Category
import org.example.project.domain.model.Currency
import org.example.project.domain.model.Transaction
import org.example.project.domain.model.TransactionType
import org.example.project.domain.usecase.category.GetCategoriesUseCase
import org.example.project.domain.usecase.transaction.AddTransactionUseCase

class AddEditTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditTransactionState())
    val state: StateFlow<AddEditTransactionState> = _state.asStateFlow()

    init {
        loadCategories(TransactionType.EXPENSE)
    }

    fun onTypeSelected(type: TransactionType) {
        _state.update { it.copy(selectedType = type, selectedCategory = null) }
        loadCategories(type)
    }

    private fun loadCategories(type: TransactionType) {
        viewModelScope.launch {
            getCategoriesUseCase(type).collect { categoryList ->
                _state.update {
                    it.copy(
                        categories = categoryList,
                        selectedCategory = it.selectedCategory ?: categoryList.firstOrNull()
                    )
                }
            }
        }
    }

    fun onAmountChanged(amount: String) {
        _state.update { it.copy(amount = amount) }
    }

    fun onCurrencySelected(currency: Currency) {
        _state.update { it.copy(selectedCurrency = currency) }
    }

    fun onCategorySelected(category: Category) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun onDateSelected(dateMillis: Long) {
        _state.update { it.copy(dateTimestamp = dateMillis) }
    }

    fun onNoteChanged(note: String) {
        _state.update { it.copy(note = note) }
    }

    fun saveTransaction() {
        val currentState = _state.value
        val amountDouble = currentState.amount.toDoubleOrNull() ?: 0.0

        if (amountDouble <= 0.0) {
            _state.update { it.copy(error = "Введите корректную сумму операции") }
            return
        }

        if (currentState.selectedCategory == null) {
            _state.update { it.copy(error = "Выберите категорию") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val transaction = Transaction(
                amount = amountDouble,
                currency = currentState.selectedCurrency,
                category = currentState.selectedCategory,
                type = currentState.selectedType,
                dateTimestamp = currentState.dateTimestamp,
                note = currentState.note
            )

            val result = addTransactionUseCase(transaction)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSaved = true) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Ошибка сохранения") }
                }
            )
        }
    }
}
