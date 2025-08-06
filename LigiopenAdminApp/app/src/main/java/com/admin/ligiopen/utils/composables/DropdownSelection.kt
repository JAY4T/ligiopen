package com.loan.fanakaloan.utils.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.admin.ligiopen.utils.screenFontSize
import com.admin.ligiopen.utils.screenWidth

@Composable
fun DropdownSelection(
    expanded: Boolean,
    items: List<String>,
    selectedItem: String,
    onSelect: (item: String) -> Unit,
    onExpand: () -> Unit,
    isError: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isError)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(screenWidth(10.0)),
        border = if (isError)
            BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        else null,
        onClick = onExpand,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(screenWidth(16.0))) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedItem,
                    fontSize = screenFontSize(14.0).sp,
                    color = if (isError && selectedItem == "Select")
                        MaterialTheme.colorScheme.error
                    else if (selectedItem == "Select")
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown arrow",
                    tint = if (isError)
                        MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            DropdownMenu(
//                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                expanded = expanded,
                onDismissRequest = onExpand,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenWidth(16.0))
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontSize = screenFontSize(14.0).sp
                            )
                        },
                        onClick = {
                            onSelect(item)
                            onExpand()
                        }
                    )
                }
            }
        }
    }
}