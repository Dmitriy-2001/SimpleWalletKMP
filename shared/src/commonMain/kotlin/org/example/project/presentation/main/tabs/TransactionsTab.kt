package org.example.project.presentation.main.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.transactions.list.TransactionsScreen

object TransactionsTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "История",
            icon = null
        )

    @Composable
    override fun Content() {
        TransactionsScreen()
    }
}
