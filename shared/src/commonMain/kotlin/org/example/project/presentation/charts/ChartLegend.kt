package org.example.project.presentation.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.domain.model.Category
import org.example.project.domain.model.CategoryExpenseShare
import org.example.project.domain.model.Currency
import org.example.project.domain.model.TransactionType
import org.example.project.presentation.components.parseHexColor
import org.example.project.presentation.theme.SimpleWalletTheme

@Composable
fun ChartLegend(
    shares: List<CategoryExpenseShare>,
    currency: Currency = Currency.KZT,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        shares.forEach { share ->
            val color = parseHexColor(share.category.colorHex)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(color)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = share.category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${share.percentage.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "${share.totalAmount.toInt()} ${currency.symbol}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChartLegendPreview() {
    SimpleWalletTheme {
        ChartLegend(
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
                )
            ),
            currency = Currency.KZT
        )
    }
}
