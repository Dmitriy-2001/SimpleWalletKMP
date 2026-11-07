package org.example.project.presentation.transactions.add_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.model.Category
import org.example.project.domain.model.Currency
import org.example.project.domain.model.TransactionType
import org.example.project.presentation.components.AmountInputField
import org.example.project.presentation.components.CategoryChip
import org.example.project.presentation.components.DatePickerModal
import org.example.project.presentation.components.SimpleTopBar
import org.example.project.presentation.theme.SimpleWalletTheme
import org.koin.compose.viewmodel.koinViewModel

class AddEditTransactionScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<AddEditTransactionViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(state.isSaved) {
            if (state.isSaved) {
                navigator.pop()
            }
        }

        AddEditTransactionScreenContent(
            state = state,
            onBackClick = { navigator.pop() },
            onTypeSelected = viewModel::onTypeSelected,
            onAmountChanged = viewModel::onAmountChanged,
            onCurrencySelected = viewModel::onCurrencySelected,
            onCategorySelected = viewModel::onCategorySelected,
            onDateSelected = viewModel::onDateSelected,
            onNoteChanged = viewModel::onNoteChanged,
            onSaveClick = viewModel::saveTransaction
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditTransactionScreenContent(
    state: AddEditTransactionState,
    onBackClick: () -> Unit,
    onTypeSelected: (TransactionType) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDateSelected: (Long) -> Unit,
    onNoteChanged: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerModal(
            initialSelectedDateMillis = state.dateTimestamp,
            onDateSelected = { millis ->
                onDateSelected(millis)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = if (state.selectedType == TransactionType.EXPENSE) "Новый расход" else "Новый доход",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TabRow(
                selectedTabIndex = if (state.selectedType == TransactionType.EXPENSE) 0 else 1,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Tab(
                    selected = state.selectedType == TransactionType.EXPENSE,
                    onClick = { onTypeSelected(TransactionType.EXPENSE) },
                    text = { Text("Расход", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = state.selectedType == TransactionType.INCOME,
                    onClick = { onTypeSelected(TransactionType.INCOME) },
                    text = { Text("Доход", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Валюта:", style = MaterialTheme.typography.labelMedium)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Currency.entries.forEach { curr ->
                    FilterChip(
                        selected = state.selectedCurrency == curr,
                        onClick = { onCurrencySelected(curr) },
                        label = { Text(curr.code) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AmountInputField(
                value = state.amount,
                onValueChange = onAmountChanged,
                selectedCurrency = state.selectedCurrency,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Категория:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                state.categories.forEach { category ->
                    CategoryChip(
                        category = category,
                        isSelected = state.selectedCategory?.id == category.id,
                        onClick = { onCategorySelected(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val localDate = Instant.fromEpochMilliseconds(state.dateTimestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val dateText = "${localDate.dayOfMonth.toString().padStart(2, '0')}.${localDate.monthNumber.toString().padStart(2, '0')}.${localDate.year}"

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Дата: $dateText")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.note,
                onValueChange = onNoteChanged,
                label = { Text("Заметка (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Сохранить транзакцию", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun AddEditTransactionScreenPreview() {
    SimpleWalletTheme {
        AddEditTransactionScreenContent(
            state = AddEditTransactionState(
                amount = "15000",
                selectedType = TransactionType.EXPENSE,
                categories = listOf(
                    Category(1, "Продукты", "shopping_cart", "#4CAF50", TransactionType.EXPENSE),
                    Category(2, "Транспорт", "bus", "#2196F3", TransactionType.EXPENSE)
                )
            ),
            onBackClick = {},
            onTypeSelected = {},
            onAmountChanged = {},
            onCurrencySelected = {},
            onCategorySelected = {},
            onDateSelected = {},
            onNoteChanged = {},
            onSaveClick = {}
        )
    }
}
