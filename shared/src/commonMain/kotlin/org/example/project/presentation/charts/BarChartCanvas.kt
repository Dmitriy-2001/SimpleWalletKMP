package org.example.project.presentation.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.domain.model.DailyBreakdown
import org.example.project.presentation.theme.ExpenseRed
import org.example.project.presentation.theme.IncomeGreen
import org.example.project.presentation.theme.SimpleWalletTheme
import kotlin.math.max

@Composable
fun BarChartCanvas(
    dailyData: List<DailyBreakdown>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (dailyData.isEmpty()) {
            Text(
                text = "Нет данных за месяц",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            val gridColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
            val maxAmount = max(
                dailyData.maxOfOrNull { max(it.income, it.expense) } ?: 1.0,
                1.0
            )

            Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                val chartWidth = size.width
                val chartHeight = size.height
                val count = dailyData.size
                val barGroupWidth = chartWidth / count
                val barWidth = (barGroupWidth * 0.35f).coerceAtLeast(4f)

                drawLine(
                    color = gridColor,
                    start = Offset(0f, chartHeight),
                    end = Offset(chartWidth, chartHeight),
                    strokeWidth = 2f
                )

                drawLine(
                    color = gridColor,
                    start = Offset(0f, chartHeight / 2f),
                    end = Offset(chartWidth, chartHeight / 2f),
                    strokeWidth = 1f
                )

                dailyData.forEachIndexed { index, data ->
                    val xGroupStart = index * barGroupWidth

                    if (data.income > 0) {
                        val incomeHeight = ((data.income / maxAmount) * chartHeight).toFloat()
                        drawRoundRect(
                            color = IncomeGreen,
                            topLeft = Offset(xGroupStart + barGroupWidth * 0.1f, chartHeight - incomeHeight),
                            size = Size(barWidth, incomeHeight),
                            cornerRadius = CornerRadius(4f)
                        )
                    }

                    if (data.expense > 0) {
                        val expenseHeight = ((data.expense / maxAmount) * chartHeight).toFloat()
                        drawRoundRect(
                            color = ExpenseRed,
                            topLeft = Offset(xGroupStart + barGroupWidth * 0.5f, chartHeight - expenseHeight),
                            size = Size(barWidth, expenseHeight),
                            cornerRadius = CornerRadius(4f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BarChartCanvasPreview() {
    SimpleWalletTheme {
        BarChartCanvas(
            dailyData = listOf(
                DailyBreakdown(dayOfMonth = 1, income = 50000.0, expense = 12000.0),
                DailyBreakdown(dayOfMonth = 2, income = 0.0, expense = 8500.0),
                DailyBreakdown(dayOfMonth = 3, income = 0.0, expense = 15000.0),
                DailyBreakdown(dayOfMonth = 4, income = 20000.0, expense = 3000.0),
                DailyBreakdown(dayOfMonth = 5, income = 0.0, expense = 9200.0),
                DailyBreakdown(dayOfMonth = 6, income = 0.0, expense = 4100.0),
                DailyBreakdown(dayOfMonth = 7, income = 15000.0, expense = 2000.0)
            )
        )
    }
}
