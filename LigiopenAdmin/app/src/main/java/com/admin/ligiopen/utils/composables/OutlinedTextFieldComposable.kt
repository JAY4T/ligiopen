package com.admin.ligiopen.utils.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun OutlinedTextFieldComposable(
    label: String,
    value: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions,
    onValueChange: (value: String) -> Unit,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        label = {
            Text(
                text = label
            )
        },
        value = value,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        shape = shape,
        onValueChange = onValueChange,
        modifier = modifier
    )
}