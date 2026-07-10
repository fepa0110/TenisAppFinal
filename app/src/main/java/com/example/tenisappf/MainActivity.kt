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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tenisappf.screens.HomeScreen
import com.example.tenisappf.screens.LoginScreen
import com.example.tenisappf.screens.PlayersScreen
import com.example.tenisappf.screens.TournamentScreen
import com.example.tenisappf.screens.TournamentsScreen
import com.example.tenisappf.ui.theme.TenisAppFTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

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
    val tenisDatabase = Firebase.firestore
    val navController = rememberNavController()

    val onNavigateToTournament: (String) -> Unit =
        { tournamentId ->
            navController.navigate("tournament/$tournamentId")
        }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(tenisDatabase, onNavigateToTournament = onNavigateToTournament)
        }
        composable("tournament/{tournamentId}") { backStackEntry ->
            TournamentScreen(
                onNavigateBack = { navController.popBackStack() },
                tenisDatabase = tenisDatabase,
                tournamentId = backStackEntry.arguments?.getString("tournamentId"),
            )
        }
    }


}