package org.example.project.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType
import org.example.project.domain.usecase.category.AddCustomCategoryUseCase
import org.example.project.domain.usecase.category.DeleteCategoryUseCase
import org.example.project.domain.usecase.category.GetCategoriesUseCase

class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCustomCategoryUseCase: AddCustomCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        loadCategories(TransactionType.EXPENSE)
    }

    fun selectType(type: TransactionType) {
        _state.update { it.copy(selectedType = type) }
        loadCategories(type)
    }

    private fun loadCategories(type: TransactionType) {
        viewModelScope.launch {
            getCategoriesUseCase(type).collect { list ->
                _state.update { it.copy(categories = list, isLoading = false) }
            }
        }
    }

    fun showAddCategoryDialog() {
        _state.update { it.copy(isAddCategoryDialogVisible = true, newCategoryName = "", error = null) }
    }

    fun hideAddCategoryDialog() {
        _state.update { it.copy(isAddCategoryDialogVisible = false, error = null) }
    }

    fun onNewCategoryNameChanged(name: String) {
        _state.update { it.copy(newCategoryName = name) }
    }

    fun onNewCategoryColorChanged(colorHex: String) {
        _state.update { it.copy(newCategoryColorHex = colorHex) }
    }

    fun onNewCategoryIconChanged(iconName: String) {
        _state.update { it.copy(newCategoryIconName = iconName) }
    }

    fun addCustomCategory() {
        val currentState = _state.value
        if (currentState.newCategoryName.isBlank()) {
            _state.update { it.copy(error = "Введите название категории") }
            return
        }

        viewModelScope.launch {
            val newCategory = Category(
                name = currentState.newCategoryName.trim(),
                iconName = currentState.newCategoryIconName,
                colorHex = currentState.newCategoryColorHex,
                type = currentState.selectedType,
                isCustom = true
            )

            val result = addCustomCategoryUseCase(newCategory)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isAddCategoryDialogVisible = false, newCategoryName = "") }
                },
                onFailure = { e ->
                    _state.update { it.copy(error = e.message ?: "Ошибка добавления категории") }
                }
            )
        }
    }

    fun deleteCustomCategory(id: Long) {
        viewModelScope.launch {
            deleteCategoryUseCase(id)
        }
    }
}
