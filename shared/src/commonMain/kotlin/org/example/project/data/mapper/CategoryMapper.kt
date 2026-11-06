package org.example.project.data.mapper

import org.example.project.data.database.entity.CategoryEntity
import org.example.project.domain.model.Category
import org.example.project.domain.model.TransactionType

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    iconName = iconName,
    colorHex = colorHex,
    type = try { TransactionType.valueOf(type) } catch (e: Exception) { TransactionType.EXPENSE },
    isCustom = isCustom
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    iconName = iconName,
    colorHex = colorHex,
    type = type.name,
    isCustom = isCustom
)
