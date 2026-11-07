package org.example.project.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.example.project.presentation.components.CategoryIcon
import org.example.project.presentation.main.tabs.AnalyticsTab
import org.example.project.presentation.main.tabs.CategoriesTab
import org.example.project.presentation.main.tabs.TransactionsTab
import org.example.project.presentation.theme.SimpleWalletTheme

class MainTabScreen : Screen {

    @Composable
    override fun Content() {
        TabNavigator(TransactionsTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(TransactionsTab, "history")
                        TabNavigationItem(AnalyticsTab, "trending_up")
                        TabNavigationItem(CategoriesTab, "more_horiz")
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    CurrentTab()
                }
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab, iconName: String) {
        val tabNavigator = LocalTabNavigator.current
        val isSelected = tabNavigator.current.key == tab.key

        NavigationBarItem(
            selected = isSelected,
            onClick = { tabNavigator.current = tab },
            icon = {
                CategoryIcon(
                    iconName = iconName,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text(tab.options.title) }
        )
    }
}

@Preview
@Composable
private fun MainTabScreenPreview() {
    SimpleWalletTheme {
        MainTabScreen().Content()
    }
}
