package org.example.project.presentation.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.domain.model.Category
import org.example.project.domain.model.CategoryExpenseShare
import org.example.project.domain.model.Currency
import org.example.project.domain.model.TransactionType
import org.example.project.presentation.components.parseHexColor
import org.example.project.presentation.theme.SimpleWalletTheme

@Composable
fun PieChartCanvas(
    shares: List<CategoryExpenseShare>,
    currency: Currency = Currency.KZT,
    modifier: Modifier = Modifier
) {
    val totalAmount = shares.sumOf { it.totalAmount }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        if (shares.isEmpty() || totalAmount == 0.0) {
            val emptyColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            Canvas(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                val strokeWidth = size.minDimension * 0.18f
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)

                drawArc(
                    color = emptyColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "Нет расходов",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Canvas(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                val strokeWidth = size.minDimension * 0.18f
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)

                var startAngle = -90f

                shares.forEach { share ->
                    val sweepAngle = (share.percentage / 100f) * 360f
                    val sliceColor = parseHexColor(share.category.colorHex)

                    drawArc(
                        color = sliceColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle - 2f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = Size(diameter, diameter),
                        style = Stroke(width = strokeWidth)
                    )
                    startAngle += sweepAngle
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Всего",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${totalAmount.toInt()} ${currency.symbol}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun PieChartCanvasPreview() {
    SimpleWalletTheme {
        PieChartCanvas(
            shares = listOf(
                CategoryExpenseShare(
                    category = Category(1, "Продукты", "shopping_cart", "#4CAF50", TransactionType.EXPENSE),
                    totalAmount = 45000.0,
                    percentage = 50f
                ),
                CategoryExpenseShare(
                    category = Category(2, "Транспорт", "bus", "#2196F3", TransactionType.EXPENSE),
                    totalAmount = 27000.0,
                    percentage = 30f
                ),
                CategoryExpenseShare(
                    category = Category(3, "Развлечения", "games", "#9C27B0", TransactionType.EXPENSE),
                    totalAmount = 18000.0,
                    percentage = 20f
                )
            ),
            currency = Currency.KZT
        )
    }
}
