package com.example.tenisappf.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.functionallightsnew.types.ItemMenu
import com.example.tenisapp.components.FloatingButton
import com.example.tenisappf.model.UserRole
import com.example.tenisappf.model.firebase.UserPermission
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObjects

const val SCREEN_TOURNAMENTS = "Torneos"
const val SCREEN_PLAYERS = "Jugadores"

val items = listOf(
    ItemMenu(
        SCREEN_TOURNAMENTS, Icons.Default.SportsTennis
    ), ItemMenu(
        SCREEN_PLAYERS, Icons.Default.People
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tenisDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    signOutUser: () -> Unit,
    onNavigateToTournament: (String) -> Unit
) {
    val currentUser = firebaseAuth.currentUser
    val userRole = remember { mutableStateOf<String>(UserRole.USER.descripcion) }

    BackHandler(onBack = {}, enabled = false)
    val selectedItemIndex = rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect("HomeScreen") {
        tenisDatabase.collection("userPermissions")
            .whereEqualTo("uid", currentUser?.uid)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val userPermissions = result.toObjects<UserPermission>()
                userRole.value = userPermissions.first().role.toString()
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
                    items[selectedItemIndex.intValue].name,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                /*IconButton(onClick = { scopeDrawerState.launch { drawerState.open() } }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = "Menu"
                    )
                }*/
            },
            actions = {
                IconButton(onClick = { signOutUser() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ExitToApp,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = "Cerrar sesion"
                    )
                }

            }
        )
    }, floatingActionButtonPosition = FabPosition.End, floatingActionButton = {

    }, content = { innerPadding ->
        when (items[selectedItemIndex.intValue].name) {
            SCREEN_TOURNAMENTS -> TournamentsScreen(
                innerPading = innerPadding,
                tenisDatabase = tenisDatabase,
                onNavigatetoTournament = onNavigateToTournament
            )

            SCREEN_PLAYERS -> PlayersScreen(
                innerPading = innerPadding,
                tenisDatabase = tenisDatabase
            )
        }
    }, bottomBar = {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary, floatingActionButton = {
                if (userRole.value == UserRole.ADMINISTRATOR.descripcion) {
                    FloatingButton(
                        icon = Icons.Filled.Add,
                        onClick = {

                        }
                    )
                }
            },
            contentPadding = PaddingValues(horizontal = 20.dp),
            actions = {
                items.forEachIndexed { itemIndex, itemMenu ->
                    if (selectedItemIndex.intValue == itemIndex) {
                        FilledTonalIconToggleButton(checked = true, onCheckedChange = { }) {
                            Icon(itemMenu.icon, contentDescription = itemMenu.name)
                        }
                    } else {
                        IconButton(onClick = { selectedItemIndex.intValue = itemIndex }) {
                            Icon(
                                itemMenu.icon,
                                contentDescription = itemMenu.name,
                            )
                        }
                    }
                }
            })
    })
}