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
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import com.example.tenisappf.items
import com.example.tenisappf.model.Tournament
import com.example.tenisappf.viewModel.TournamentsListViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

const val TAG = "TournamentsScreen"

@Composable
fun TournamentsScreen(
    modifier: Modifier = Modifier,
    innerPading: PaddingValues,
    tenisDatabase: FirebaseFirestore,
//    viewModel: TournamentsListViewModel
) {
//    val tournaments by viewModel.tournamentsList.observeAsState(listOf())

    val tournaments = remember { mutableStateListOf<Tournament>() }

    LaunchedEffect(Unit) {
        tenisDatabase.collection("tournaments")
            .get()
            .addOnSuccessListener { result ->
                // Clear existing data to avoid duplicates on recomposition
                tournaments.clear()
                val tournamentsList = result.toObjects<Tournament>()
                tournaments.addAll(tournamentsList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    @Composable
    fun TournamentListItem(title: String?, subtitle: String, status: String) {
        ListItem(
            headlineContent = { Text(title!!) },
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPading),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        items(tournaments) { tournament ->
            TournamentListItem(tournament.nombre, tournament.fecha!!.toDate().toString(), "Activo")
        }
    }
}