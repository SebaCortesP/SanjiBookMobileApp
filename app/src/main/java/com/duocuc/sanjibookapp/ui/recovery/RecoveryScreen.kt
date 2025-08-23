package com.duocuc.sanjibookapp.ui.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val azulApp = Color(0xFF243B94)

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Input Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input Nueva contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input Confirmar contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Repite la contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mostrar errores
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // btn confirmar
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        errorMessage = "Todos los campos son obligatorios"
                    } else if (password != confirmPassword) {
                        errorMessage = "Las contraseñas no coinciden"
                    } else {
                        errorMessage = ""
                        // volver al login
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = azulApp)
            ) {
                Text("Confirmar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Volver
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver", color = azulApp)
            }
        }
    }
}