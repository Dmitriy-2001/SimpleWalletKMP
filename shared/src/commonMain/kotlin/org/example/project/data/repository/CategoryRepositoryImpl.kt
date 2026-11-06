package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.database.DefaultDataSeeder
import org.example.project.data.database.dao.AccountDao
import org.example.project.data.database.dao.CategoryDao
import org.example.project.data.mapper.toDomain
import org.example.project.data.mapper.toEntity
import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type.name).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override suspend fun addCategory(category: Category): Long {
        return categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun deleteCustomCategory(id: Long) {
        categoryDao.deleteCustomCategoryById(id)
    }

    override suspend fun ensureDefaultCategoriesSeeded() {
        DefaultDataSeeder.seedIfEmpty(categoryDao, accountDao)
    }
}
