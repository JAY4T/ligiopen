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
fun ResultDialog(
    title: String,
    body: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    showDismissButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                fontSize = screenFontSize(16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = body,
                fontSize = screenFontSize(14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            if(showDismissButton) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = screenFontSize(14.0).sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(
                    text = "Ok",
                    fontSize = screenFontSize(14.0).sp
                )
            }
        }
    )
}