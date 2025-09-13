package com.duocuc.sanjibookapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duocuc.sanjibookapp.R
import com.duocuc.sanjibookapp.models.Session
import com.duocuc.sanjibookapp.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    val azulSanji = Color(0xFF243B94)

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image( painter = painterResource(id = R.drawable.logo_sanji), contentDescription = "Logo App", modifier = Modifier.size(150.dp) )
            Spacer(modifier = Modifier.height(16.dp))
            Text( text = "Bienvenido al Libro de Sanji", style = MaterialTheme.typography.headlineSmall.copy( fontSize = 28.sp ), textAlign = TextAlign.Center )
            Spacer(modifier = Modifier.height(18.dp))
            Text( text = "Inicie sesión para continuar", style = MaterialTheme.typography.headlineSmall.copy( fontSize = 20.sp ), textAlign = TextAlign.Center )
            Spacer(modifier = Modifier.height(24.dp))
            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    loginError = null
                },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) {
                Text(
                    emailError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                    loginError = null
                },
                label = { Text("Contraseña") },
                singleLine = true,
                isError = passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null) {
                Text(
                    passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp
                )
            }
            // Botón de Login
            Button(
                onClick = {
                    val user = User(email, password)

                    val errors = user.validate()
                    emailError = errors["email"]
                    passwordError = errors["password"]

                    if (errors.isEmpty()) {
                        if (user.authenticate()) {
                            Session.currentUser = user
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            loginError = User.lastError
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azulSanji,
                    contentColor = Color.White
                )
            ) {
                Text("Iniciar Sesión", fontSize = 18.sp)
            }

            if (loginError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = loginError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

