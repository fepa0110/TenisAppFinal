package com.example.tenisapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text : String = "",
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = Color.White,
        ),
        contentPadding = PaddingValues(horizontal = 64.dp, vertical = 16.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if(!enabled) Color.Gray else MaterialTheme.colorScheme.onPrimary
        )
    }
}