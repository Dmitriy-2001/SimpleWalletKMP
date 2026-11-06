package org.example.project.domain.model

enum class Currency(val code: String, val symbol: String, val displayName: String) {
    KZT("KZT", "₸", "Тенге (KZT)"),
    TRY("TRY", "₺", "Лира (TRY)"),
    USD("USD", "$", "Доллар (USD)"),
    EUR("EUR", "€", "Евро (EUR)"),
    RUB("RUB", "₽", "Рубль (RUB)");

    companion object {
        fun fromCode(code: String): Currency =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: KZT
    }
}
