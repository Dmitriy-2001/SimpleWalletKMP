package org.example.project.domain.model

data class CategoryExpenseShare(
    val category: Category,
    val totalAmount: Double,
    val percentage: Float
)
