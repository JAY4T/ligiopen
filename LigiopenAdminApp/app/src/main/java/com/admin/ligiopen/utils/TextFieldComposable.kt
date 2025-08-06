package com.admin.ligiopen.utils

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TextFieldComposable(
    label: String,
    value: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = {
            Text(
                text = label
            )
        },
        value = value,
        keyboardOptions = keyboardOptions,
        colors = colors,
        enabled = enabled,
        onValueChange = onValueChange,
        modifier = modifier
    )
}