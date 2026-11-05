package org.example.project.data.database

import org.example.project.data.database.dao.AccountDao
import org.example.project.data.database.dao.CategoryDao
import org.example.project.data.database.entity.AccountEntity
import org.example.project.data.database.entity.CategoryEntity

object DefaultDataSeeder {
    val defaultCategories = listOf(
        // Expense categories
        CategoryEntity(name = "Продукты", iconName = "shopping_cart", colorHex = "#4CAF50", type = "EXPENSE", isCustom = false),
        CategoryEntity(name = "Транспорт", iconName = "directions_bus", colorHex = "#2196F3", type = "EXPENSE", isCustom = false),
        CategoryEntity(name = "Коммуналка", iconName = "receipt_long", colorHex = "#FF9800", type = "EXPENSE", isCustom = false),
        CategoryEntity(name = "Развлечения", iconName = "sports_esports", colorHex = "#9C27B0", type = "EXPENSE", isCustom = false),
        CategoryEntity(name = "Здоровье", iconName = "medical_services", colorHex = "#E91E63", type = "EXPENSE", isCustom = false),
        CategoryEntity(name = "Покупки", iconName = "shopping_bag", colorHex = "#E040FB", type = "EXPENSE", isCustom = false),
        CategoryEntity(name = "Другое", iconName = "more_horiz", colorHex = "#607D8B", type = "EXPENSE", isCustom = false),

        // Income categories
        CategoryEntity(name = "Зарплата", iconName = "payments", colorHex = "#4CAF50", type = "INCOME", isCustom = false),
        CategoryEntity(name = "Фриланс", iconName = "computer", colorHex = "#00BCD4", type = "INCOME", isCustom = false),
        CategoryEntity(name = "Инвестиции", iconName = "trending_up", colorHex = "#3F51B5", type = "INCOME", isCustom = false),
        CategoryEntity(name = "Подарок", iconName = "card_giftcard", colorHex = "#FF4081", type = "INCOME", isCustom = false),
        CategoryEntity(name = "Другой доход", iconName = "attach_money", colorHex = "#795548", type = "INCOME", isCustom = false)
    )

    val defaultAccount = AccountEntity(
        name = "Основной кошелек",
        balance = 0.0,
        currency = "KZT",
        iconName = "account_balance_wallet",
        colorHex = "#2196F3"
    )

    suspend fun seedIfEmpty(categoryDao: CategoryDao, accountDao: AccountDao) {
        if (categoryDao.getCategoryCount() == 0) {
            categoryDao.insertCategories(defaultCategories)
        }
        if (accountDao.getAccountCount() == 0) {
            accountDao.insertAccount(defaultAccount)
        }
    }
}
