package org.example.project.di

import org.example.project.data.database.AppDatabase
import org.example.project.data.database.getRoomDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
    single { get<AppDatabase>().transactionDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().accountDao() }
}
