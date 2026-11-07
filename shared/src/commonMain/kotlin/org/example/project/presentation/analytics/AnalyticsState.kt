package org.example.project.presentation.analytics

import org.example.project.domain.model.CategoryExpenseShare
import org.example.project.domain.model.Currency
import org.example.project.domain.model.DailyBreakdown
import org.example.project.domain.model.MonthlySummary

data class AnalyticsState(
    val selectedYear: Int = 2025,
    val selectedMonth: Int = 5,
    val summary: MonthlySummary = MonthlySummary(0.0, 0.0, 0.0, Currency.KZT),
    val expenseShares: List<CategoryExpenseShare> = emptyList(),
    val dailyBreakdown: List<DailyBreakdown> = emptyList(),
    val selectedCurrency: Currency = Currency.KZT,
    val isLoading: Boolean = false,
    val error: String? = null
)
