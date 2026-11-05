package org.example.project.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.project.data.database.dao.AccountDao
import org.example.project.data.database.dao.CategoryDao
import org.example.project.data.database.dao.TransactionDao
import org.example.project.data.database.entity.AccountEntity
import org.example.project.data.database.entity.CategoryEntity
import org.example.project.data.database.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
}
