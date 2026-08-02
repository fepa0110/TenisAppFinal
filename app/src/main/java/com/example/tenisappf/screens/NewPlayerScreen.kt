package com.example.tenisappf.screens

import android.R.attr.contentDescription
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenisapp.components.FloatingButton
import com.example.tenisapp.components.PrimaryButton
import com.example.tenisapp.viewModel.LoginViewModel
import com.example.tenisapp.viewModel.NewPlayerViewModel
import com.example.tenisappf.components.TertiaryButton
import com.example.tenisappf.model.UserRole
import com.example.tenisappf.model.firebase.Game
import com.example.tenisappf.model.ui.GameUI
import com.example.tenisappf.model.firebase.Player
import com.example.tenisappf.model.firebase.PlayerGenre
import com.example.tenisappf.model.firebase.Tournament
import com.example.tenisappf.model.firebase.UserPermission
import com.example.tenisappf.utils.DateFormats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.launch

private const val TAG = "TournamentScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlayerScreen(
    onNavigateBack: () -> Unit,
    tenisDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    viewModel: NewPlayerViewModel = viewModel()
) {
    val currentUser = firebaseAuth.currentUser
    val userRole = remember { mutableStateOf<String>(UserRole.USER.descripcion) }

    var generoJugador = remember { mutableStateOf<String>(PlayerGenre.MASCULINO.descripcion) }
    var generoRadioState = remember { mutableStateOf<Boolean>(true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scopeSnackBar = rememberCoroutineScope()

    val openSnackBar: (String) -> Unit = { text ->
        scopeSnackBar.launch {
            snackbarHostState.showSnackbar(
                text
            )
        }
    }

    // Ejecutar onNavigateBack cuando se presiona el boton atras fisico
    BackHandler(onBack = onNavigateBack)

    LaunchedEffect("NewPlayer") {
        tenisDatabase.collection("userPermissions")
            .whereEqualTo("uid", currentUser?.uid)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val userPermissions = result.toObjects<UserPermission>()
                userRole.value = userPermissions.first().role.toString()
            }
    }

    fun createPlayer() {
        val playerData = hashMapOf(
            "name" to viewModel.name,
            "genre" to viewModel.genre
        )

        tenisDatabase.collection("players")
            .add(playerData)
            .addOnSuccessListener { docRef ->
                onNavigateBack()
                Log.d(TAG, "Player created")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding player")
            }
    }

    @Composable
    fun PlayerNameField(name: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = name, onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            label = { Text("Nombre") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Composable
    fun GeneroRadioGroup(){
        Row(modifier = Modifier.selectableGroup()){
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Masculino",
                style = MaterialTheme.typography.displaySmall
            )

            RadioButton(
                selected = generoRadioState.value,
                onClick = {
                        generoRadioState.value = true
                        generoJugador.value = PlayerGenre.MASCULINO.descripcion
                    },
                modifier = Modifier.semantics { },
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Femenino",
                style = MaterialTheme.typography.displaySmall
            )
            RadioButton(
                selected = !generoRadioState.value,
                onClick = { generoRadioState.value = false
                    generoJugador.value = PlayerGenre.FEMENINO.descripcion },
                modifier = Modifier.semantics { },
            )
        }
    }

    @Composable
    fun NewPlayerForm() {
        val name: String by viewModel.name.observeAsState(initial = "")
        val coroutineScope = rememberCoroutineScope()

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .fillMaxHeight()
//                .background(color = MaterialTheme.colorScheme.background)
        ) {

            Spacer(modifier = Modifier.padding(16.dp))
            PlayerNameField(name) { viewModel.onPlayerChanged(it, generoJugador.value) }
            Spacer(modifier = Modifier.padding(4.dp))
            GeneroRadioGroup()

            PrimaryButton(
                text = "Enviar",
                enabled = true,
                onClick = { createPlayer() }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TertiaryButton(
                text = "Volver",
                onClick = { onNavigateBack() },
                enabled = true
            )

        }
    }

    @Composable
    fun NewPlayerContent(innerPading: PaddingValues) {
        Column(modifier = Modifier.padding(innerPading)) {
            NewPlayerForm()
        }

    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.DarkGray
                ),
                title = {
                    Text(
                        "Nuevo jugador",
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
        NewPlayerContent(innerPadding)
    })
}