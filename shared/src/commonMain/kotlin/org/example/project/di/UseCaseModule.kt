package org.example.project.di

import org.example.project.domain.usecase.account.AddAccountUseCase
import org.example.project.domain.usecase.account.GetAccountsUseCase
import org.example.project.domain.usecase.analytics.GetDailyBreakdownUseCase
import org.example.project.domain.usecase.analytics.GetExpenseShareUseCase
import org.example.project.domain.usecase.analytics.GetMonthlySummaryUseCase
import org.example.project.domain.usecase.category.AddCustomCategoryUseCase
import org.example.project.domain.usecase.category.DeleteCategoryUseCase
import org.example.project.domain.usecase.category.GetCategoriesUseCase
import org.example.project.domain.usecase.transaction.AddTransactionUseCase
import org.example.project.domain.usecase.transaction.DeleteTransactionUseCase
import org.example.project.domain.usecase.transaction.EditTransactionUseCase
import org.example.project.domain.usecase.transaction.GetMonthlyTransactionsUseCase
import org.example.project.domain.usecase.transaction.GetTransactionsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { AddTransactionUseCase(get(), get()) }
    factory { DeleteTransactionUseCase(get(), get()) }
    factory { EditTransactionUseCase(get(), get()) }
    factory { GetTransactionsUseCase(get()) }
    factory { GetMonthlyTransactionsUseCase(get()) }

    factory { GetCategoriesUseCase(get()) }
    factory { AddCustomCategoryUseCase(get()) }
    factory { DeleteCategoryUseCase(get()) }

    factory { GetAccountsUseCase(get()) }
    factory { AddAccountUseCase(get()) }

    factory { GetMonthlySummaryUseCase(get()) }
    factory { GetExpenseShareUseCase(get()) }
    factory { GetDailyBreakdownUseCase(get()) }
}
