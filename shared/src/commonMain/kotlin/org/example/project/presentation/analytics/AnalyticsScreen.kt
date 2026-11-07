package org.example.project.presentation.analytics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.domain.model.Category
import org.example.project.domain.model.CategoryExpenseShare
import org.example.project.domain.model.Currency
import org.example.project.domain.model.DailyBreakdown
import org.example.project.domain.model.MonthlySummary
import org.example.project.domain.model.TransactionType
import org.example.project.presentation.charts.BarChartCanvas
import org.example.project.presentation.charts.ChartLegend
import org.example.project.presentation.charts.PieChartCanvas
import org.example.project.presentation.components.SimpleTopBar
import org.example.project.presentation.theme.ExpenseRed
import org.example.project.presentation.theme.IncomeGreen
import org.example.project.presentation.theme.SimpleWalletTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalyticsScreen() {
    val viewModel = koinViewModel<AnalyticsViewModel>()
    val state by viewModel.state.collectAsState()

    AnalyticsScreenContent(
        state = state,
        onPreviousMonth = viewModel::previousMonth,
        onNextMonth = viewModel::nextMonth
    )
}

@Composable
fun AnalyticsScreenContent(
    state: AnalyticsState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthNames = listOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )
    val monthTitle = "${monthNames.getOrElse(state.selectedMonth - 1) { "" }} ${state.selectedYear}"

    Scaffold(
        topBar = {
            SimpleTopBar(title = "Аналитика")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "< Пред.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onPreviousMonth() }.padding(8.dp)
                )

                Text(
                    text = monthTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "След. >",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNextMonth() }.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = "Доходы",
                        amount = "+${state.summary.totalIncome.toInt()} ${state.selectedCurrency.symbol}",
                        color = IncomeGreen,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Расходы",
                        amount = "-${state.summary.totalExpense.toInt()} ${state.selectedCurrency.symbol}",
                        color = ExpenseRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Структура расходов",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PieChartCanvas(
                            shares = state.expenseShares,
                            currency = state.selectedCurrency
                        )
                        if (state.expenseShares.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            ChartLegend(
                                shares = state.expenseShares,
                                currency = state.selectedCurrency
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Динамика по дням",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        BarChartCanvas(dailyData = state.dailyBreakdown)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = color
            )
        }
    }
}

@Preview
@Composable
private fun AnalyticsScreenPreview() {
    SimpleWalletTheme {
        AnalyticsScreenContent(
            state = AnalyticsState(
                selectedYear = 2025,
                selectedMonth = 5,
                summary = MonthlySummary(
                    totalIncome = 350000.0,
                    totalExpense = 90000.0,
                    netBalance = 260000.0,
                    currency = Currency.KZT
                ),
                expenseShares = listOf(
                    CategoryExpenseShare(
                        category = Category(1, "Продукты", "shopping_cart", "#4CAF50", TransactionType.EXPENSE),
                        totalAmount = 45000.0,
                        percentage = 50f
                    ),
                    CategoryExpenseShare(
                        category = Category(2, "Транспорт", "bus", "#2196F3", TransactionType.EXPENSE),
                        totalAmount = 27000.0,
                        percentage = 30f
                    )
                ),
                dailyBreakdown = listOf(
                    DailyBreakdown(dayOfMonth = 1, income = 50000.0, expense = 12000.0),
                    DailyBreakdown(dayOfMonth = 2, income = 0.0, expense = 8500.0)
                )
            ),
            onPreviousMonth = {},
            onNextMonth = {}
        )
    }
}
