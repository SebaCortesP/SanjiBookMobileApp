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
import androidx.navigation.NavController
import com.duocuc.sanjibookapp.R
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    val azulSanji = Color(0xFF243B94)

    // Usuarios permitidos (maqueta)
    val allowedUsers = listOf(
        "user1@mail.com" to "123456",
        "user2@mail.com" to "abcdef",
        "user3@mail.com" to "password",
        "user4@mail.com" to "qwerty",
        "user5@mail.com" to "demo123"
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_sanji),
                contentDescription = "Logo App",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

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
                Text(emailError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
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
                Text(passwordError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Login
            Button(
                onClick = {
                    var valid = true

                    if (email.isBlank()) {
                        emailError = "El email es obligatorio"
                        valid = false
                    } else if (!isValidEmail(email)) {
                        emailError = "Formato de email inválido"
                        valid = false
                    }

                    if (password.isBlank()) {
                        passwordError = "La contraseña es obligatoria"
                        valid = false
                    }

                    if (valid) {
                        val userExists = allowedUsers.any { it.first == email && it.second == password }
                        if (userExists) {
                            // Login exitoso
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            loginError = "Credenciales incorrectas"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azulSanji,
                    contentColor = Color.White
                )
            ) {
                Text("Iniciar Sesión")
            }

            // Error de login
            loginError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de texto (links)
            TextButton(
                onClick = { navController.navigate("recovery") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = azulSanji
                )
            ) {
                Text("¿Olvidaste tu contraseña?")
            }

            TextButton(
                onClick = { navController.navigate("register") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = azulSanji
                )
            ) {
                Text("Crear cuenta")
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    return emailPattern.matcher(email).matches()
}
