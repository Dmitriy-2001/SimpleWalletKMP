package org.example.project.di

import org.example.project.data.repository.AccountRepositoryImpl
import org.example.project.data.repository.CategoryRepositoryImpl
import org.example.project.data.repository.TransactionRepositoryImpl
import org.example.project.domain.repository.AccountRepository
import org.example.project.domain.repository.CategoryRepository
import org.example.project.domain.repository.TransactionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TransactionRepository> { TransactionRepositoryImpl(get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }
}
