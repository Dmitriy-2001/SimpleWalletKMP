package org.example.project.presentation.transactions.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.project.domain.model.Account
import org.example.project.domain.model.Category
import org.example.project.domain.model.Currency
import org.example.project.domain.model.Transaction
import org.example.project.domain.model.TransactionType
import org.example.project.presentation.components.SimpleTopBar
import org.example.project.presentation.components.TransactionItemRow
import org.example.project.presentation.theme.SimpleWalletTheme
import org.example.project.presentation.transactions.add_edit.AddEditTransactionScreen
import org.koin.compose.viewmodel.koinViewModel

class TransactionsListScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<TransactionsViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        TransactionsScreenContent(
            state = state,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onDeleteTransaction = viewModel::deleteTransaction,
            onAddTransactionClick = {
                navigator.push(AddEditTransactionScreen())
            }
        )
    }
}

@Composable
fun TransactionsScreen() {
    TransactionsListScreen().Content()
}

@Composable
fun TransactionsScreenContent(
    state: TransactionsState,
    onSearchQueryChanged: (String) -> Unit,
    onDeleteTransaction: (Long) -> Unit,
    onAddTransactionClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopBar(title = "История операций")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            val balanceText = state.account?.let { "${it.balance} ${it.currency.symbol}" } ?: "0 ₸"
            val accountName = state.account?.name ?: "Основной кошелек"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = accountName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = balanceText,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Поиск транзакции...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            val filteredTransactions = state.transactions.filter { tx ->
                val query = state.searchQuery.lowercase()
                query.isEmpty() ||
                        (tx.category?.name?.lowercase()?.contains(query) == true) ||
                        tx.note.lowercase().contains(query)
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Операции не найдены",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredTransactions, key = { it.id }) { transaction ->
                        TransactionItemRow(
                            transaction = transaction,
                            onDeleteClick = { onDeleteTransaction(transaction.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TransactionsScreenPreview() {
    SimpleWalletTheme {
        TransactionsScreenContent(
            state = TransactionsState(
                isLoading = false,
                account = Account(1, "Основной кошелек", 150000.0, Currency.KZT),
                transactions = listOf(
                    Transaction(
                        id = 1,
                        amount = 12500.0,
                        currency = Currency.KZT,
                        category = Category(1, "Продукты", "shopping_cart", "#4CAF50", TransactionType.EXPENSE),
                        type = TransactionType.EXPENSE,
                        dateTimestamp = 1716000000000L,
                        note = "Супермаркет"
                    ),
                    Transaction(
                        id = 2,
                        amount = 250000.0,
                        currency = Currency.KZT,
                        category = Category(2, "Зарплата", "payments", "#4CAF50", TransactionType.INCOME),
                        type = TransactionType.INCOME,
                        dateTimestamp = 1715900000000L,
                        note = "Аванс"
                    )
                )
            ),
            onSearchQueryChanged = {},
            onDeleteTransaction = {},
            onAddTransactionClick = {}
        )
    }
}
