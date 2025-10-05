package com.duocuc.sanjibookapp.ui.register

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duocuc.sanjibookapp.data.database.AppDatabase
import com.duocuc.sanjibookapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val azulApp = Color(0xFF243B94)

    // Estados de campos
    var email by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var sexoSelected by remember { mutableStateOf("") }
    var terminosChecked by remember { mutableStateOf(false) }

    // Estados de error
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var apellidoError by remember { mutableStateOf<String?>(null) }
    var fechaError by remember { mutableStateOf<String?>(null) }
    var sexoError by remember { mutableStateOf<String?>(null) }
    var terminosError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    // Otros estados
    var showSuccessDialog by remember { mutableStateOf(false) }
    var sexoExpanded by remember { mutableStateOf(false) }
    val sexoOptions = listOf("Masculino", "Femenino", "Prefiero omitir")

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; nombreError = null },
                label = { Text("Nombre") },
                singleLine = true,
                isError = nombreError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (nombreError != null) Text(nombreError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))

            // Apellido
            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it; apellidoError = null },
                label = { Text("Apellido") },
                singleLine = true,
                isError = apellidoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (apellidoError != null) Text(apellidoError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Contraseña") },
                singleLine = true,
                isError = passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // Repetir contraseña
            OutlinedTextField(
                value = repetirPassword,
                onValueChange = { repetirPassword = it; passwordError = null },
                label = { Text("Repetir contraseña") },
                singleLine = true,
                isError = passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null) Text(passwordError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))

            // Fecha de nacimiento
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de nacimiento (dd/MM/yyyy)") },
                singleLine = true,
                isError = fechaError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                fechaNacimiento = "%02d/%02d/%04d".format(day, month + 1, year)
                                fechaError = null
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            )
            if (fechaError != null) Text(fechaError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))

            // Sexo
            ExposedDropdownMenuBox(
                expanded = sexoExpanded,
                onExpandedChange = { sexoExpanded = !sexoExpanded }
            ) {
                OutlinedTextField(
                    value = sexoSelected,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded) },
                    isError = sexoError != null,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = sexoExpanded,
                    onDismissRequest = { sexoExpanded = false }
                ) {
                    sexoOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sexoSelected = option
                                sexoExpanded = false
                                sexoError = null
                            }
                        )
                    }
                }
            }
            if (sexoError != null) Text(sexoError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))

            // Términos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = terminosChecked,
                    onCheckedChange = { terminosChecked = it; terminosError = null }
                )
                Spacer(Modifier.width(8.dp))
                Text("Acepto términos y condiciones", fontSize = 18.sp)
            }
            if (terminosError != null) Text(terminosError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(24.dp))

            // Botón Registrarse
            Button(
                onClick = {
                    // Validación simple
                    nombreError = if (nombre.isBlank()) "Obligatorio" else null
                    apellidoError = if (apellido.isBlank()) "Obligatorio" else null
                    emailError = if (email.isBlank()) "Obligatorio" else null
                    passwordError = when {
                        password.isBlank() || repetirPassword.isBlank() -> "Ambas contraseñas son obligatorias"
                        password != repetirPassword -> "Las contraseñas no coinciden"
                        else -> null
                    }
                    fechaError = if (fechaNacimiento.isBlank()) "Obligatorio" else null
                    sexoError = if (sexoSelected.isBlank()) "Obligatorio" else null
                    terminosError = if (!terminosChecked) "Debes aceptar los términos" else null

                    val hasError = listOf(
                        nombreError, apellidoError, emailError,
                        passwordError, fechaError, sexoError, terminosError
                    ).any { it != null }

                    if (!hasError) {
                        // Registrar usuario en DB
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = AppDatabase.getDatabase(context)
                            val existingUser = db.userDao().getUserByEmail(email)
                            if (existingUser == null) {
                                val newUser = User(
                                    email = email,
                                    password = password,
                                    nombre = nombre,
                                    apellido = apellido,
                                    fechaNacimiento = fechaNacimiento,
                                    sexo = sexoSelected,
                                    terminosAceptados = terminosChecked,
                                    roleId = 3
                                )
                                db.userDao().insert(newUser)
                                showSuccessDialog = true
                            } else {
                                generalError = "El usuario ya existe"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = azulApp)
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            // Dialog de éxito
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showSuccessDialog = false },
                    title = { Text("Registro exitoso") },
                    text = { Text("Usuario registrado correctamente.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showSuccessDialog = false
                            navController.navigate("home") {
                                popUpTo("register") { inclusive = true }
                            }
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            // Dialog de error
            if (generalError != null) {
                AlertDialog(
                    onDismissRequest = { generalError = null },
                    title = { Text("Error") },
                    text = { Text(generalError!!) },
                    confirmButton = {
                        TextButton(onClick = { generalError = null }) {
                            Text("OK")
                        }
                    }
                )
            }

            // Enlace a login
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Ya tengo cuenta, iniciar sesión", color = azulApp, fontSize = 18.sp)
            }
        }
    }
}
