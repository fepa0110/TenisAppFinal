package com.example.tenisappf.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TournamentsScreen(modifier: Modifier = Modifier, innerPading: PaddingValues) {
    @Composable
    fun TournamentListItem(title: String, subtitle: String, status: String) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = { Text(subtitle) },
            trailingContent = { Text(status) },
            leadingContent = {
                Icon(Icons.Filled.SportsTennis, contentDescription = "Localized description")
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPading),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        repeat(5) { cantidad ->
            HorizontalDivider()
            TournamentListItem("Torneo $cantidad", "Londres", "En curso")
            HorizontalDivider()
        }
    }
}