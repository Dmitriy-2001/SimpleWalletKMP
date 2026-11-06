package org.example.project.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType
import org.example.project.domain.repository.CategoryRepository

class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(type: TransactionType? = null): Flow<List<Category>> {
        return if (type != null) {
            categoryRepository.getCategoriesByType(type)
        } else {
            categoryRepository.getAllCategories()
        }
    }
}
