package org.example.project.presentation.categories

import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType

data class CategoriesState(
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val categories: List<Category> = emptyList(),
    val isAddCategoryDialogVisible: Boolean = false,
    val newCategoryName: String = "",
    val newCategoryColorHex: String = "#4CAF50",
    val newCategoryIconName: String = "shopping_cart",
    val isLoading: Boolean = false,
    val error: String? = null
)
