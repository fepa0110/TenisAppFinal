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
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.automirrored.filled.Forward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import com.example.tenisappf.model.Tournament
import com.example.tenisappf.viewModel.TournamentsListViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

const val TAG = "TournamentsScreen"

@Composable
fun TournamentsScreen(
    modifier: Modifier = Modifier,
    innerPading: PaddingValues,
    tenisDatabase: FirebaseFirestore,
    onNavigatetoTournament: (String) -> Unit
) {
//    val tournaments by viewModel.tournamentsList.observeAsState(listOf())

    val tournaments = remember { mutableStateMapOf<String, Tournament>() }

    LaunchedEffect(Unit) {
        tenisDatabase.collection("tournaments")
            .get()
            .addOnSuccessListener { result ->
                // Clear existing data to avoid duplicates on recomposition
                tournaments.clear()
                result.forEach { document ->
                    val tournament = document.toObject<Tournament>()
                    tournaments[document.id] = Tournament(tournament.nombre, tournament.fecha)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    @Composable
    fun TournamentListItem(tournamentId: String?, title: String?, subtitle: String) {
        ListItem(
//            onClick = { onNavigatetoTournament(tournamentId!!)},
            headlineContent = { Text(title!!) },
            supportingContent = { Text(subtitle) },
            trailingContent = {
                IconButton(onClick = { onNavigatetoTournament(tournamentId!!)}){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Forward,
                        tint = Color.White,
                        contentDescription = "Go to tournament"
                    )
            }},
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
        tournaments.forEach { (tournamentKey, tournament) ->
            item {
                TournamentListItem(tournamentKey, tournament.nombre, tournament.fecha!!.toDate().toString())
            }
        }
    }
}