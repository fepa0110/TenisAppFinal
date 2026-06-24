package com.example.tenisapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TertiaryButton(
    text : String = "",
    onClick: () -> Unit,
    enabled: Boolean
) {
    TextButton(
        onClick = { onClick() },
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 64.dp, vertical = 16.dp)
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            color = if(!enabled) Color.Gray else MaterialTheme.colorScheme.primary
        )
    }
}