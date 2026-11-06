package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun addCategory(category: Category): Long
    suspend fun deleteCustomCategory(id: Long)
    suspend fun ensureDefaultCategoriesSeeded()
}
