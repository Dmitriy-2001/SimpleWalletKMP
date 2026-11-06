package org.example.project.domain.usecase.category

import org.example.project.domain.repository.CategoryRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Long): Result<Unit> {
        categoryRepository.deleteCustomCategory(categoryId)
        return Result.success(Unit)
    }
}
