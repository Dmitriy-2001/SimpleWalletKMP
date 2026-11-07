package org.example.project.presentation.main.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.categories.CategoriesScreen

object CategoriesTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = "Категории",
            icon = null
        )

    @Composable
    override fun Content() {
        CategoriesScreen()
    }
}
