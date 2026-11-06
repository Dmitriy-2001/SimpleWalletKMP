package org.example.project.domain.model

data class MonthlySummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double,
    val currency: Currency
)
