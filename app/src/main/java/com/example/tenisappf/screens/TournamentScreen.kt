package com.example.tenisappf.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentScreen(
    onNavigateBack: () -> Unit,
    tournamentId: String?
) {
    // Ejecutar onNavigateBack cuando se presiona el boton atras fisico
    BackHandler(onBack = onNavigateBack)

    @Composable
    fun TournamentContent(innerPading: PaddingValues) {
        Column(modifier = Modifier.padding(innerPading)) {
            Text(
                "Torneo ${tournamentId}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.DarkGray
            ),
            title = {
                Text(
                    "Torneo",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = "Notificaciones"
                    )
                }

            }
        )
    }, floatingActionButtonPosition = FabPosition.End, floatingActionButton = {

    }, content = { innerPadding ->
        TournamentContent(innerPadding)
    })
}