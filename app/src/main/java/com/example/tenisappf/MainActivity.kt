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
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.tenisappf.screens.LoginScreen
import com.example.tenisappf.ui.theme.TenisAppFTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TenisAppFTheme {
                TenisAppFApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun TenisAppFApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    val tenisDatabase = Firebase.firestore

    tenisDatabase.collection("tournaments")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                Log.d("DATABASE", "${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.w("DATABASE", "Error getting documents.", exception)
        }

    LoginScreen {  }
    /*Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Torneos",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                *//* navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    }
                } *//*
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {

        },
        content = { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                *//*floatingActionButton = {
                    FloatingButton(icon = Icons.Filled.Add,
                        onClick = {
                            lifecycleScope.launch {
                                gamesViewModel.create(Tournament(nombre = "Torneo 1", fecha = Date()))
                            }
                        }
                )},*//*
                contentPadding = PaddingValues(horizontal = 20.dp),
                actions = {
                    FilledTonalIconToggleButton(checked = true, onCheckedChange = { }) {
                        Icon(Icons.Filled.SportsTennis, contentDescription = "Tournaments")
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Filled.Scoreboard,
                            contentDescription = "Games",
                        )
                    }
                }
            )
        }
    )*/


}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    FAVORITES("Torneos", R.drawable.ic_favorite),
    PROFILE("Jugadores", R.drawable.ic_account_box),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TenisAppFTheme {
        Greeting("Android")
    }
}