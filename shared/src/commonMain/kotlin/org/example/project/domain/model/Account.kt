package org.example.project.domain.model

data class Account(
    val id: Long = 0,
    val name: String,
    val balance: Double,
    val currency: Currency,
    val iconName: String = "account_balance_wallet",
    val colorHex: String = "#2196F3"
)
