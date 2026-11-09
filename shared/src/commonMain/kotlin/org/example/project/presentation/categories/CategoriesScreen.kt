package org.example.project.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType
import org.example.project.presentation.components.CategoryIcon
import org.example.project.presentation.components.SimpleTopBar
import org.example.project.presentation.components.parseHexColor
import org.example.project.presentation.theme.SimpleWalletTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoriesScreen() {
    val viewModel = koinViewModel<CategoriesViewModel>()
    val state by viewModel.state.collectAsState()

    CategoriesScreenContent(
        state = state,
        onTypeSelected = viewModel::selectType,
        onShowAddDialog = viewModel::showAddCategoryDialog,
        onHideAddDialog = viewModel::hideAddCategoryDialog,
        onNewNameChanged = viewModel::onNewCategoryNameChanged,
        onNewColorChanged = viewModel::onNewCategoryColorChanged,
        onNewIconChanged = viewModel::onNewCategoryIconChanged,
        onAddCategoryConfirm = viewModel::addCustomCategory,
        onDeleteCategory = viewModel::deleteCustomCategory
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesScreenContent(
    state: CategoriesState,
    onTypeSelected: (TransactionType) -> Unit,
    onShowAddDialog: () -> Unit,
    onHideAddDialog: () -> Unit,
    onNewNameChanged: (String) -> Unit,
    onNewColorChanged: (String) -> Unit,
    onNewIconChanged: (String) -> Unit,
    onAddCategoryConfirm: () -> Unit,
    onDeleteCategory: (Long) -> Unit
) {
    val predefinedColors = listOf(
        "#4CAF50", "#2196F3", "#FF9800", "#9C27B0",
        "#E91E63", "#00BCD4", "#FF5722", "#607D8B"
    )
    val predefinedIcons = listOf(
        "shopping_cart", "directions_bus", "receipt_long", "sports_esports",
        "medical_services", "payments", "computer", "trending_up",
        "card_giftcard", "home", "work", "person"
    )

    if (state.isAddCategoryDialogVisible) {
        AlertDialog(
            onDismissRequest = onHideAddDialog,
            title = { Text("Новая категория", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = state.newCategoryName,
                        onValueChange = onNewNameChanged,
                        label = { Text("Название категории") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Выберите цвет:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        predefinedColors.forEach { colorHex ->
                            val color = parseHexColor(colorHex)
                            val isSelected = state.newCategoryColorHex == colorHex
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { onNewColorChanged(colorHex) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Выберите иконку:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        predefinedIcons.forEach { iconName ->
                            val isSelected = state.newCategoryIconName == iconName
                            val color = parseHexColor(state.newCategoryColorHex)
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) color else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onNewIconChanged(iconName) },
                                contentAlignment = Alignment.Center
                            ) {
                                CategoryIcon(iconName = iconName, tint = color, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    if (state.error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = onAddCategoryConfirm) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = onHideAddDialog) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            SimpleTopBar(title = "Категории")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onShowAddDialog,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            TabRow(
                selectedTabIndex = if (state.selectedType == TransactionType.EXPENSE) 0 else 1,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Tab(
                    selected = state.selectedType == TransactionType.EXPENSE,
                    onClick = { onTypeSelected(TransactionType.EXPENSE) },
                    text = { Text("Расходы", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = state.selectedType == TransactionType.INCOME,
                    onClick = { onTypeSelected(TransactionType.INCOME) },
                    text = { Text("Доходы", fontWeight = FontWeight.Bold) }
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.categories, key = { it.id }) { category ->
                        CategoryItemRow(
                            category = category,
                            onDeleteClick = if (category.isCustom) {
                                { onDeleteCategory(category.id) }
                            } else null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItemRow(
    category: Category,
    onDeleteClick: (() -> Unit)? = null
) {
    val color = parseHexColor(category.colorHex)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                CategoryIcon(iconName = category.iconName, tint = color, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                if (category.isCustom) {
                    Text(
                        text = "Пользовательская",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (onDeleteClick != null) {
                TextButton(onClick = onDeleteClick) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview
@Composable
private fun CategoriesScreenPreview() {
    SimpleWalletTheme {
        CategoriesScreenContent(
            state = CategoriesState(
                selectedType = TransactionType.EXPENSE,
                categories = listOf(
                    Category(1, "Продукты", "shopping_cart", "#4CAF50", TransactionType.EXPENSE, isCustom = false),
                    Category(2, "Такси", "bus", "#2196F3", TransactionType.EXPENSE, isCustom = true)
                )
            ),
            onTypeSelected = {},
            onShowAddDialog = {},
            onHideAddDialog = {},
            onNewNameChanged = {},
            onNewColorChanged = {},
            onNewIconChanged = {},
            onAddCategoryConfirm = {},
            onDeleteCategory = {}
        )
    }
}
