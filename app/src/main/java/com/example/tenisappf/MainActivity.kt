package com.example.tenisappf

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.functionallightsnew.types.ItemMenu
import com.example.tenisappf.screens.LoginScreen
import com.example.tenisappf.screens.PlayersScreen
import com.example.tenisappf.screens.TournamentsScreen
import com.example.tenisappf.ui.theme.TenisAppFTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

const val SCREEN_TOURNAMENTS = "Torneos"
const val SCREEN_PLAYERS = "Jugadores"

val items = listOf(
    ItemMenu(
        SCREEN_TOURNAMENTS, Icons.Default.SportsTennis
    ), ItemMenu(
        SCREEN_PLAYERS, Icons.Default.People
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TenisAppFTheme(darkTheme = true, dynamicColor = false) {
                TenisAppFApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun TenisAppFApp() {
    val selectedItemIndex = rememberSaveable { mutableIntStateOf(0) }

    val tenisDatabase = Firebase.firestore

//    LoginScreen {  }
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
        when (items[selectedItemIndex.intValue].name) {
            SCREEN_TOURNAMENTS -> TournamentsScreen(innerPading = innerPadding, tenisDatabase = tenisDatabase)

            SCREEN_PLAYERS -> PlayersScreen(innerPading = innerPadding)
        }
    }, bottomBar = {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary, floatingActionButton = {
                /*FloatingButton(icon = Icons.Filled.Add,
                    onClick = {
                        lifecycleScope.launch {
                            gamesViewModel.create(Tournament(nombre = "Torneo 1", fecha = Date()))
                        }
                    }
                )*/
            },
            contentPadding = PaddingValues(horizontal = 20.dp),
            actions = {
                items.forEachIndexed { itemIndex, itemMenu ->
                    if (selectedItemIndex.intValue == itemIndex) {
                        FilledTonalIconToggleButton(checked = true, onCheckedChange = { }) {
                            Icon(itemMenu.icon, contentDescription = itemMenu.name)
                        }
                    } else {
                        IconButton(onClick = { selectedItemIndex.value = itemIndex }) {
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TenisAppFTheme {
    }
}