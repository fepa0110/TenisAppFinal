package com.example.tenisappf.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import com.example.tenisapp.components.PrimaryButton
import com.example.tenisappf.components.TertiaryButton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenisapp.viewModel.LoginViewModel
import com.example.tenisapp.viewModel.SignUpViewModel

import com.example.tenisappf.R
import com.example.tenisappf.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

//import com.example.tenisapp.components.PrimaryButton
//import com.example.tenisapp.components.TertiaryButton
//import com.example.tenisapp.data.model.User

//import com.example.tenisapp.AppViewModelProvider

private const val TAG = "SignUpScreen"

@Composable
fun SignUpScreen(
    tenisDatabase: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    BackHandler(onBack = onNavigateBack)

    val snackbarHostState = remember { SnackbarHostState() }
    val scopeSnackBar = rememberCoroutineScope()

    val openSnackBar: (String) -> Unit = { text ->
        scopeSnackBar.launch {
            snackbarHostState.showSnackbar(
                text
            )
        }
    }

    fun createUser(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = firebaseAuth.currentUser
                    val userPermission = hashMapOf(
                        "uid" to user?.uid,
                        "role" to UserRole.USER.descripcion
                    )

                    tenisDatabase.collection("userPermissions")
                        .add(userPermission)
                        .addOnSuccessListener { Log.d(TAG, "UserPermissions successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                    onNavigateToHome()
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    openSnackBar("Authentication failed.")
//                    updateUI(null)
                }
            }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = password, onValueChange = { onTextFieldChanged(it) },
            label = { Text("Contraeña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
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
    fun EmailField(username: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = username, onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
    fun HeaderImage() {
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .paint(
                    painter = painterResource(id = R.drawable.welcome),
                    contentScale = ContentScale.FillWidth
                ),
            contentAlignment = Alignment.Center
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Bienvenido",
                style = MaterialTheme.typography.displayLarge
            )
        }
    }

    @Composable
    fun SignUpForm() {
        val email: String by viewModel.username.observeAsState(initial = "")
        val password: String by viewModel.password.observeAsState(initial = "")
        val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
        val coroutineScope = rememberCoroutineScope()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight()
                .width(400.dp)
//                .background(color = MaterialTheme.colorScheme.background)
        ) {

            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(password) {
                viewModel.onLoginChanged(
                    email, it
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))

            PrimaryButton(
                text = "Registarse",
                enabled = true,
                onClick = { createUser(email, password)}
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TertiaryButton(
                text = "Volver",
                onClick = { },
                enabled = true
            )

        }
    }

    @Composable
    fun SignUpContent(innerPadding: PaddingValues) {
//    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

        /*    if (isLoading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            } else {*/
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            HeaderImage()
            Spacer(modifier = Modifier.padding(16.dp))
            SignUpForm()
        }
//    }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            SignUpContent(innerPadding)
        })
}

