package org.example.project.data.mapper

import org.example.project.data.database.entity.AccountEntity
import org.example.project.domain.model.Account
import org.example.project.domain.model.Currency

fun AccountEntity.toDomain(): Account = Account(
    id = id,
    name = name,
    balance = balance,
    currency = Currency.fromCode(currency),
    iconName = iconName,
    colorHex = colorHex
)

fun Account.toEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    balance = balance,
    currency = currency.code,
    iconName = iconName,
    colorHex = colorHex
)
