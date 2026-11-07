package org.example.project.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CategoryIcon(
    iconName: String,
    tint: Color,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokeWidth = width * 0.08f

        when (iconName.lowercase()) {
            "shopping_cart", "cart", "products", "продукты" -> {
                val path = Path().apply {
                    moveTo(width * 0.15f, height * 0.25f)
                    lineTo(width * 0.35f, height * 0.25f)
                    lineTo(width * 0.45f, height * 0.65f)
                    lineTo(width * 0.85f, height * 0.65f)
                    lineTo(width * 0.90f, height * 0.35f)
                    lineTo(width * 0.38f, height * 0.35f)
                }
                drawPath(path, color = tint, style = Stroke(width = strokeWidth))
                drawCircle(tint, radius = width * 0.08f, center = Offset(width * 0.50f, height * 0.82f))
                drawCircle(tint, radius = width * 0.08f, center = Offset(width * 0.80f, height * 0.82f))
            }
            "directions_bus", "bus", "transport", "транспорт" -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(width * 0.2f, height * 0.15f),
                    size = Size(width * 0.6f, height * 0.6f),
                    cornerRadius = CornerRadius(width * 0.1f),
                    style = Stroke(width = strokeWidth)
                )
                drawLine(tint, start = Offset(width * 0.2f, height * 0.45f), end = Offset(width * 0.8f, height * 0.45f), strokeWidth = strokeWidth)
                drawCircle(tint, radius = width * 0.06f, center = Offset(width * 0.35f, height * 0.62f))
                drawCircle(tint, radius = width * 0.06f, center = Offset(width * 0.65f, height * 0.62f))
            }
            "payments", "salary", "зарплата", "money" -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(width * 0.15f, height * 0.25f),
                    size = Size(width * 0.7f, height * 0.5f),
                    cornerRadius = CornerRadius(width * 0.08f),
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(tint, radius = width * 0.12f, center = Offset(width * 0.5f, height * 0.5f), style = Stroke(width = strokeWidth))
            }
            "trending_up", "investment", "инвестиции" -> {
                val path = Path().apply {
                    moveTo(width * 0.15f, height * 0.8f)
                    lineTo(width * 0.45f, height * 0.55f)
                    lineTo(width * 0.65f, height * 0.65f)
                    lineTo(width * 0.85f, height * 0.25f)
                }
                drawPath(path, color = tint, style = Stroke(width = strokeWidth))
                drawLine(tint, start = Offset(width * 0.65f, height * 0.25f), end = Offset(width * 0.85f, height * 0.25f), strokeWidth = strokeWidth)
                drawLine(tint, start = Offset(width * 0.85f, height * 0.45f), end = Offset(width * 0.85f, height * 0.25f), strokeWidth = strokeWidth)
            }
            else -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(width * 0.2f, height * 0.25f),
                    size = Size(width * 0.6f, height * 0.5f),
                    cornerRadius = CornerRadius(width * 0.08f),
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(tint, radius = width * 0.06f, center = Offset(width * 0.65f, height * 0.5f))
            }
        }
    }
}

fun parseHexColor(hex: String, defaultColor: Color = Color.Gray): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(colorInt or 0xFF000000)
        } else {
            Color(colorInt)
        }
    } catch (_: Exception) {
        defaultColor
    }
}
