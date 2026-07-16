package com.example.tenisappf.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenisappf.components.LoadingIndicator
import com.example.tenisappf.components.ScoreCard
import com.example.tenisappf.model.firebase.Game
import com.example.tenisappf.model.firebase.Player
import com.example.tenisappf.model.firebase.Tournament
import com.example.tenisappf.model.ui.GameUI
import com.example.tenisappf.utils.DateFormats
import com.example.tenisappf.viewModel.GameViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject

private const val TAG = "GameScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onNavigateBack: () -> Unit,
    tenisDatabase: FirebaseFirestore,
    gameId: String?,
    tournamentName: String?,
    viewModel: GameViewModel = viewModel()
) {

    val game by viewModel.game.observeAsState(GameUI())
//    val game by viewModel.game

    val loading = remember { mutableStateOf<Boolean>(true) }

    val role = "admin"

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

                                    viewModel.updateGame(
                                        GameUI(
                                            jugador1 = jugador1,
                                            jugador2 = jugador2,
                                            puntajeJugador1 = gameDocument.puntajeJugador1,
                                            puntajeJugador2 = gameDocument.puntajeJugador2,
                                            estado = gameDocument.estado
                                        )
                                    )

                                    loading.value = false
                                }
                        }
                }

        }
    }

    tenisDatabase.collection("games").document(gameId!!)
        .addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val gameSnapshot = snapshot.toObject<Game>()

                viewModel.updatePuntajes(
                    gameSnapshot!!.puntajeJugador1!!,
                    gameSnapshot.puntajeJugador2!!
                )

                Log.d(TAG, "Current data: ${game.puntajeJugador1}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

    fun increasePlayerScore(playerNumber: Int) {
        tenisDatabase.collection("games").document(gameId)
            .update("puntajeJugador$playerNumber", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun decreasePlayerScore(playerNumber: Int) {
        tenisDatabase.collection("games").document(gameId)
            .update("puntajeJugador$playerNumber", FieldValue.increment(-1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
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
                    LoadingIndicator()
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
                            game.jugador1!!.nombre!!,
                            game.puntajeJugador1!!.toString()
                        )

                        Text(
                            game.puntajeJugador1!!.toString(),
                            modifier = Modifier.padding(top = 6.dp, bottom = 3.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        if (role == "admin") {
                            Row(
                                modifier = Modifier
                                    .width(180.dp)
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledIconButton(
                                    modifier = Modifier.size(64.dp),
                                    onClick = { decreasePlayerScore(1) }) {
                                    Icon(
                                        imageVector = Icons.Filled.ExposureNeg1,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        contentDescription = "DecScore1"
                                    )
                                }

                                FilledIconButton(
                                    modifier = Modifier.size(64.dp),
                                    onClick = { increasePlayerScore(1) }) {
                                    Icon(
                                        imageVector = Icons.Filled.PlusOne,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        contentDescription = "AddScore1"
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.defaultMinSize(minHeight = 180.dp),
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
                            game.jugador2!!.nombre!!,
                            game.puntajeJugador2!!.toString()
                        )

                        if (role == "admin") {
                            Row(
                                modifier = Modifier
                                    .width(180.dp)
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledIconButton(
                                    modifier = Modifier.size(64.dp),
                                    onClick = { decreasePlayerScore(2) }) {
                                    Icon(
                                        imageVector = Icons.Filled.ExposureNeg1,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        contentDescription = "DecScore2"
                                    )
                                }

                                FilledIconButton(
                                    modifier = Modifier.size(64.dp),
                                    onClick = { increasePlayerScore(2) }) {
                                    Icon(
                                        imageVector = Icons.Filled.PlusOne,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        contentDescription = "AddScore2"
                                    )
                                }
                            }
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
                    "Partido",
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