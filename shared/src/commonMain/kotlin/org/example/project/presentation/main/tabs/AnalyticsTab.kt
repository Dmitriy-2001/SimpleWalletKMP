package org.example.project.presentation.main.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.analytics.AnalyticsScreen

object AnalyticsTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "Аналитика",
            icon = null
        )

    @Composable
    override fun Content() {
        AnalyticsScreen()
    }
}
