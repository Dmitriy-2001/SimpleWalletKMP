package org.example.project.di

import org.example.project.presentation.analytics.AnalyticsViewModel
import org.example.project.presentation.transactions.add_edit.AddEditTransactionViewModel
import org.example.project.presentation.transactions.list.TransactionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TransactionsViewModel(get(), get(), get(), get()) }
    viewModel { AddEditTransactionViewModel(get(), get()) }
    viewModel { AnalyticsViewModel(get(), get(), get()) }
}
