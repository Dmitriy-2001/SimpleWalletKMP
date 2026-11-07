package org.example.project.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.model.Currency
import org.example.project.domain.usecase.analytics.GetDailyBreakdownUseCase
import org.example.project.domain.usecase.analytics.GetExpenseShareUseCase
import org.example.project.domain.usecase.analytics.GetMonthlySummaryUseCase
import kotlin.time.Clock

class AnalyticsViewModel(
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
    private val getExpenseShareUseCase: GetExpenseShareUseCase,
    private val getDailyBreakdownUseCase: GetDailyBreakdownUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()

    init {
        val millis = Clock.System.now().toEpochMilliseconds()
        val now = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
        _state.update { it.copy(selectedYear = now.year, selectedMonth = now.monthNumber) }
        loadAnalytics()
    }

    fun previousMonth() {
        var currentMonth = _state.value.selectedMonth - 1
        var currentYear = _state.value.selectedYear
        if (currentMonth < 1) {
            currentMonth = 12
            currentYear -= 1
        }
        _state.update { it.copy(selectedMonth = currentMonth, selectedYear = currentYear) }
        loadAnalytics()
    }

    fun nextMonth() {
        var currentMonth = _state.value.selectedMonth + 1
        var currentYear = _state.value.selectedYear
        if (currentMonth > 12) {
            currentMonth = 1
            currentYear += 1
        }
        _state.update { it.copy(selectedMonth = currentMonth, selectedYear = currentYear) }
        loadAnalytics()
    }

    fun selectCurrency(currency: Currency) {
        _state.update { it.copy(selectedCurrency = currency) }
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val (start, end) = getMonthTimestampRange(_state.value.selectedYear, _state.value.selectedMonth)
            val currency = _state.value.selectedCurrency

            combine(
                getMonthlySummaryUseCase(start, end, currency),
                getExpenseShareUseCase(start, end),
                getDailyBreakdownUseCase(start, end)
            ) { summary, shares, daily ->
                _state.value.copy(
                    isLoading = false,
                    summary = summary,
                    expenseShares = shares,
                    dailyBreakdown = daily
                )
            }.collect { updatedState ->
                _state.value = updatedState
            }
        }
    }

    private fun getMonthTimestampRange(year: Int, month: Int): Pair<Long, Long> {
        val startInstant = Instant.parse("${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-01T00:00:00Z")
        val daysInMonth = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        val endInstant = Instant.parse("${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${daysInMonth.toString().padStart(2, '0')}T23:59:59Z")
        return Pair(startInstant.toEpochMilliseconds(), endInstant.toEpochMilliseconds())
    }
}
