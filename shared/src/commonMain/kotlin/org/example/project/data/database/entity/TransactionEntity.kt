package org.example.project.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val currency: String,
    val categoryId: Long,
    val type: String,
    val dateTimestamp: Long,
    val note: String = "",
    val accountId: Long = 1
)
