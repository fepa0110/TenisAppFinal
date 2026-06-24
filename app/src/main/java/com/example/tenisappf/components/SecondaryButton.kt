package com.example.tenisapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecondaryButton(
    text : String = "",
    onClick: () -> Unit,
    enabled: Boolean
) {
    OutlinedButton(
        onClick = { onClick() },
        enabled = enabled,
        border = BorderStroke(
            width = 3.dp,
            color = if(!enabled) Color.Gray else MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(horizontal = 64.dp, vertical = 16.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if(!enabled) Color.Gray else MaterialTheme.colorScheme.primary
        )
    }
}