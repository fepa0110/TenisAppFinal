package com.example.tenisappf.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tenisapp.components.FloatingButton
import com.example.tenisappf.model.UserRole
import com.example.tenisappf.model.firebase.Game
import com.example.tenisappf.model.ui.GameUI
import com.example.tenisappf.model.firebase.Player
import com.example.tenisappf.model.firebase.Tournament
import com.example.tenisappf.model.firebase.UserPermission
import com.example.tenisappf.utils.DateFormats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects

private const val TAG = "TournamentScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGame: (String, String) -> Unit,
    tenisDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    tournamentId: String?
) {
    val currentUser = firebaseAuth.currentUser
    val userRole = remember { mutableStateOf<String>(UserRole.USER.descripcion) }

    val tournament = remember { mutableStateOf<Tournament>(Tournament()) }
    val games = remember { mutableStateMapOf<String, GameUI>() }


    val loadingGames = remember { mutableStateOf<Boolean>(true) }

    // Ejecutar onNavigateBack cuando se presiona el boton atras fisico
    BackHandler(onBack = onNavigateBack)

    LaunchedEffect("Tournament$tournamentId") {
        tenisDatabase.collection("userPermissions")
            .whereEqualTo("uid", currentUser?.uid)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val userPermissions = result.toObjects<UserPermission>()
                userRole.value = userPermissions.first().role.toString()
            }

        if (tournamentId != null) {
            val tournamentReference = tenisDatabase.collection("tournaments").document(tournamentId)

            tournamentReference.get()
                .addOnSuccessListener { result ->
                    val tournamentData = result.toObject<Tournament>()
                    tournament.value = Tournament(
                        nombre = tournamentData?.nombre,
                        fecha = tournamentData?.fecha
                    )
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

            tenisDatabase.collection("games")
                .whereEqualTo("torneo", tournamentReference)
                .get()
                .addOnSuccessListener { result ->
                    games.clear()
                    result.forEach { gameDocument ->
                        val game = gameDocument.toObject<Game>()
                        var jugador1: Player = Player();
                        var jugador2: Player = Player();

                        tenisDatabase.collection("players")
                            .document(game.jugador1!!.id)
                            .get()
                            .addOnCompleteListener { taskPlayer1 ->
                                jugador1 = taskPlayer1.result.toObject<Player>()!!

                                tenisDatabase.collection("players")
                                    .document(game.jugador2!!.id)
                                    .get()
                                    .addOnCompleteListener { taskPlayer2 ->
                                        jugador2 = taskPlayer2.result.toObject<Player>()!!

                                        games[gameDocument.id] = GameUI(
                                            jugador1 = jugador1,
                                            jugador2 = jugador2,
                                            puntajeJugador1 = game.puntajeJugador1,
                                            puntajeJugador2 = game.puntajeJugador2,
                                            estado = game.estado
                                        )
                                        Log.i(TAG, "Game: " + gameDocument.id)
                                    }
                            }
                    }
                    loadingGames.value = false
                    Log.i(TAG, "Games: " + games.isNotEmpty())
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }

    }

    @Composable
    fun GameListItem(gameId: String?, title: String, subtitle: String) {
        ListItem(
//            onClick = { onNavigatetoTournament(tournamentId!!)},
            headlineContent = { Text(title) },
            supportingContent = { Text(subtitle) },
            trailingContent = {
                IconButton(onClick = { onNavigateToGame(gameId!!, tournament.value.nombre!!) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        tint = Color.White,
                        contentDescription = "Go to game"
                    )
                }
            },
            leadingContent = {
                Icon(Icons.Filled.SportsTennis, contentDescription = "Localized description")
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }

    @Composable
    fun TournamentContent(innerPading: PaddingValues) {
        Column(modifier = Modifier.padding(innerPading)) {
            Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)) {
                Text(
                    "${tournament.value.nombre}",
                    modifier = Modifier.padding(vertical = 3.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)) {

                Text(
                    DateFormats.timestampToString(tournament.value.fecha!!),
                    modifier = Modifier.padding(vertical = 3.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.primary
            )

            if (loadingGames.value) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(64.dp)
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        trackColor = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                LazyColumn() {
                    games.forEach { (gameKey, game) ->
                        item {
                            GameListItem(
                                gameKey,
                                "${game.jugador1!!.nombre} - ${game.jugador2!!.nombre}",
                                game.puntajeJugador1.toString() + " - " + game.puntajeJugador2.toString()
                            )
                        }
                    }
                }
            }
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
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Notificaciones"
                    )
                }

            }
        )
    }, floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
        if(userRole.value == UserRole.ADMINISTRATOR.descripcion){
            FloatingButton(
                onClick = {},
                icon = Icons.Default.Add,

                )
        }
    }, content = { innerPadding ->
        if (tournament.value.nombre != null) TournamentContent(innerPadding)
    })
}