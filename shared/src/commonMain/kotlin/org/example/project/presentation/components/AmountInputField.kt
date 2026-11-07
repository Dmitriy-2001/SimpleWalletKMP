package org.example.project.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.example.project.domain.model.Currency

@Composable
fun AmountInputField(
    value: String,
    onValueChange: (String) -> Unit,
    selectedCurrency: Currency,
    modifier: Modifier = Modifier,
    label: String = "Сумма"
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d{0,2}$"))) {
                onValueChange(newValue.replace(',', '.'))
            }
        },
        label = { Text(label) },
        suffix = { Text(selectedCurrency.symbol) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier
    )
}
