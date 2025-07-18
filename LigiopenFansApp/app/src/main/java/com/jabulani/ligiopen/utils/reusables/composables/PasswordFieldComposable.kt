package com.jabulani.ligiopen.utils.reusables.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.jabulani.ligiopen.R

@Composable
fun PasswordFieldComposable(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        label = {
            Text(
                text = label
            )
        },
        value = value,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(
                    painter = painterResource(id = if(passwordVisible) R.drawable.visibility_off else R.drawable.visibility_on),
                    contentDescription = "Toggle password visibility"
                )
            }
        },
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = onValueChange,
        modifier = modifier
    )
}