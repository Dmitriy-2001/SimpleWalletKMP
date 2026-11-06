package org.example.project.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: TransactionType,
    val isCustom: Boolean = false
)
