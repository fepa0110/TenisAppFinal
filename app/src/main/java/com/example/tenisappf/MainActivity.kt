package com.example.tenisappf

import android.credentials.CredentialOption
import android.credentials.GetCredentialRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.credentials.CredentialManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tenisappf.screens.GameScreen
import com.example.tenisappf.screens.HomeScreen
import com.example.tenisappf.screens.LoginScreen
import com.example.tenisappf.screens.NewPlayerScreen
import com.example.tenisappf.screens.PlayersScreen
import com.example.tenisappf.screens.SignUpScreen
import com.example.tenisappf.screens.TournamentScreen
import com.example.tenisappf.screens.TournamentsScreen
import com.example.tenisappf.ui.theme.TenisAppFTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun TenisAppFApp() {
    val tenisDatabase = Firebase.firestore
    val navController = rememberNavController()
    val firebaseAuth: FirebaseAuth = Firebase.auth

    //    lateinit var credentialManager: CredentialManager = CredentialManager.create(this)

    val onNavigateToHome: () -> Unit =
        {
            navController.navigate("home")
        }

    val onNavigateToSignUp: () -> Unit =
        {
            navController.navigate("signup")
        }

    val onNavigateToNewPlayer: () -> Unit =
        {
            navController.navigate("newPlayer")
        }

    val onNavigateToTournament: (String) -> Unit =
        { tournamentId ->
            navController.navigate("tournament/$tournamentId")
        }

    val onNavigateToGame: (String, String) -> Unit =
        { gameId, tournamentName ->
            navController.navigate("game/$gameId/$tournamentName")
        }

    val signOutUser: () -> Unit =
        {
            firebaseAuth.signOut()
            navController.navigate("login")
        }

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                tenisDatabase = tenisDatabase,
                firebaseAuth = firebaseAuth,
                onNavigateToHome = onNavigateToHome,
                onNavigateToSignUp = onNavigateToSignUp

            )
        }
        composable("signup") {
            SignUpScreen(
                tenisDatabase = tenisDatabase,
                firebaseAuth = firebaseAuth,
                onNavigateToHome = onNavigateToHome,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("newPlayer") {
            NewPlayerScreen(
                tenisDatabase = tenisDatabase,
                firebaseAuth = firebaseAuth,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                tenisDatabase,
                firebaseAuth = firebaseAuth,
                signOutUser = signOutUser,
                onNavigateToTournament = onNavigateToTournament,
                onNavigateToNewPlayer = onNavigateToNewPlayer
            )
        }
        composable("tournament/{tournamentId}") { backStackEntry ->
            TournamentScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGame = onNavigateToGame,
                tenisDatabase = tenisDatabase,
                firebaseAuth = firebaseAuth,
                tournamentId = backStackEntry.arguments?.getString("tournamentId"),
            )
        }
        composable("game/{gameId}/{tournamentName}") { backStackEntry ->
            GameScreen(
                onNavigateBack = { navController.popBackStack() },
                tenisDatabase = tenisDatabase,
                firebaseAuth = firebaseAuth,
                gameId = backStackEntry.arguments?.getString("gameId"),
                tournamentName = backStackEntry.arguments?.getString("tournamentName")
            )
        }
    }


}