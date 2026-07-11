package com.example.tenisappf.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tenisappf.components.ScoreCard
import com.example.tenisappf.model.firebase.Game
import com.example.tenisappf.model.firebase.Player
import com.example.tenisappf.model.firebase.Tournament
import com.example.tenisappf.model.ui.GameUI
import com.example.tenisappf.utils.DateFormats
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

private const val TAG = "GameScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onNavigateBack: () -> Unit,
    tenisDatabase: FirebaseFirestore,
    gameId: String?,
    tournamentName: String?
) {
    val game = remember { mutableStateOf<GameUI>(GameUI()) }

    val loading = remember { mutableStateOf<Boolean>(true) }

    // Ejecutar onNavigateBack cuando se presiona el boton atras fisico
    BackHandler(onBack = onNavigateBack)

    LaunchedEffect("Tournament$gameId") {
        if (gameId != null) {
            tenisDatabase.collection("games").document(gameId)
                .get()
                .addOnSuccessListener { result ->
                    val gameDocument = result.toObject<Game>()
                    var jugador1: Player = Player();
                    var jugador2: Player = Player();

                    tenisDatabase.collection("players")
                        .document(gameDocument?.jugador1!!.id)
                        .get()
                        .addOnCompleteListener { taskPlayer1 ->
                            jugador1 = taskPlayer1.result.toObject<Player>()!!

                            tenisDatabase.collection("players")
                                .document(gameDocument.jugador2!!.id)
                                .get()
                                .addOnCompleteListener { taskPlayer2 ->
                                    jugador2 = taskPlayer2.result.toObject<Player>()!!

                                    game.value = GameUI(
                                        jugador1 = jugador1,
                                        jugador2 = jugador2,
                                        puntajeJugador1 = gameDocument.puntajeJugador1,
                                        puntajeJugador2 = gameDocument.puntajeJugador2,
                                        estado = gameDocument.estado
                                    )

                                    loading.value = false
                                }
                        }
                }

        }

    }

    @Composable
    fun GameContent(innerPading: PaddingValues) {
        Column(modifier = Modifier.padding(innerPading)) {
            Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)) {
                Text(
                    tournamentName!!,
                    modifier = Modifier.padding(top = 6.dp, bottom = 3.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (loading.value) {
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
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ScoreCard(
                            game.value.jugador1!!.nombre!!,
                            game.value.puntajeJugador1!!.toString()
                        )

                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            " vs. ",
                            modifier = Modifier.padding(vertical = 3.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ScoreCard(
                            game.value.jugador2!!.nombre!!,
                            game.value.puntajeJugador2!!.toString()
                        )
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

    }, content = { innerPadding ->
        GameContent(innerPadding)
    })
}