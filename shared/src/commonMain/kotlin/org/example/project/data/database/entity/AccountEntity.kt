package org.example.project.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val balance: Double,
    val currency: String,
    val iconName: String = "account_balance_wallet",
    val colorHex: String = "#4CAF50"
)
