package com.admin.ligiopen.utils.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.admin.ligiopen.utils.screenFontSize

@Composable
fun LogoutDialog(
    loggingOut: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Logout confirmation",
                fontSize = screenFontSize(16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                enabled = !loggingOut,
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            Button(
                enabled = !loggingOut,
                onClick = onConfirm
            ) {
                if(loggingOut) {
                    Text(
                        text = "Logging out...",
                        fontSize = screenFontSize(14.0).sp
                    )
                } else {
                    Text(
                        text = "Logout",
                        fontSize = screenFontSize(14.0).sp
                    )
                }

            }
        },
    )
}