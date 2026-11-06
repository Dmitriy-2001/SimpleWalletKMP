package org.example.project.domain.usecase.category

import org.example.project.domain.model.Category
import org.example.project.domain.repository.CategoryRepository

class AddCustomCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Long> {
        if (category.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Название категории не может быть пустым"))
        }
        val customCategory = category.copy(isCustom = true)
        val id = categoryRepository.addCategory(customCategory)
        return Result.success(id)
    }
}
