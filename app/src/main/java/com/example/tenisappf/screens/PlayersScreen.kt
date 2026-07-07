package com.example.tenisappf.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tenisappf.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

@Composable
fun PlayersScreen(
    modifier: Modifier = Modifier,
    innerPading: PaddingValues,
    tenisDatabase: FirebaseFirestore,
) {
    val players = remember { mutableStateListOf<Player>() }

    LaunchedEffect(Unit) {
        tenisDatabase.collection("players")
            .get()
            .addOnSuccessListener { result ->
                // Clear existing data to avoid duplicates on recomposition
                players.clear()
                val playersList = result.toObjects<Player>()
                players.addAll(playersList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    @Composable
    fun PlayerListItem(title: String?, subtitle: String?) {
        ListItem(
            headlineContent = { Text(title!!) },
            supportingContent = { Text(subtitle!!) },
            leadingContent = {
                Icon(Icons.Filled.Person, contentDescription = title)
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPading),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        items(players) { player ->
            HorizontalDivider()
            PlayerListItem(player.nombre, player.nacionalidad)
            HorizontalDivider()
        }
    }
}