package com.duocuc.sanjibookapp.ui.register

import android.app.DatePickerDialog
import android.util.Patterns
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // ------- Estados de campos -------
    var email by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var sexoSelected by remember { mutableStateOf("") }
    var newsletterChecked by remember { mutableStateOf(false) }
    var terminosChecked by remember { mutableStateOf(false) }

    // ------- Estados de error -------
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var apellidoError by remember { mutableStateOf<String?>(null) }
    var fechaError by remember { mutableStateOf<String?>(null) }
    var sexoError by remember { mutableStateOf<String?>(null) }
    var terminosError by remember { mutableStateOf<String?>(null) }

    // ------- Otros estados -------
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var sexoExpanded by remember { mutableStateOf(false) }
    val sexoOptions = listOf("Masculino", "Femenino", "Prefiero omitir")

    val azulApp = Color(0xFF243B94)
    val context = LocalContext.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //  Nombre 
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

            //  Email 
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

            //  Contraseña 
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

            //  Repetir contraseña 
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

            //  Fecha de nacimiento (abre DatePicker al tocar) 
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

            //  Sexo (dropdown) 
            ExposedDropdownMenuBox(
                expanded = sexoExpanded,
                onExpandedChange = { sexoExpanded = !sexoExpanded }
            ) {
                OutlinedTextField(
                    value = sexoSelected,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded)
                    },
                    isError = sexoError != null,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

            //  Checkboxes 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = newsletterChecked,
                    onCheckedChange = { newsletterChecked = it }
                )
                Spacer(Modifier.width(8.dp))
                Text("Recibir newsletter", fontSize = 18.sp)
            }

            Spacer(Modifier.height(8.dp))

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

            //  Botón Registrarse 
            Button(
                onClick = {
                    var valid = true

                    // Reglas básicas
                    if (nombre.isBlank()) { nombreError = "Obligatorio"; valid = false }
                    if (apellido.isBlank()) { apellidoError = "Obligatorio"; valid = false }
                    if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Email inválido"; valid = false
                    }
                    if (password.isBlank() || repetirPassword.isBlank()) {
                        passwordError = "Ambas contraseñas son obligatorias"; valid = false
                    } else if (password != repetirPassword) {
                        passwordError = "Las contraseñas no coinciden"; valid = false
                    }
                    if (fechaNacimiento.isBlank()) { fechaError = "Obligatorio"; valid = false }
                    else {
                        // Mayor de 18 años
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                            isLenient = false
                        }
                        val cal = Calendar.getInstance()
                        try {
                            val dob = sdf.parse(fechaNacimiento)
                            if (dob == null) {
                                fechaError = "Fecha inválida"; valid = false
                            } else {
                                val limite = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }
                                if (!dob.before(limite.time)) {
                                    fechaError = "Debes ser mayor de 18 años"; valid = false
                                }
                            }
                        } catch (e: ParseException) {
                            fechaError = "Fecha inválida"; valid = false
                        }
                    }
                    if (sexoSelected.isBlank()) { sexoError = "Obligatorio"; valid = false }
                    if (!terminosChecked) { terminosError = "Debes aceptar los términos"; valid = false }

                    if (!valid) {
                        showErrorDialog = true
                    } else {
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = azulApp)
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            //  Dialog de errores 
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("Errores en el formulario") },
                    text = { Text("Corrige los campos en rojo antes de continuar.") },
                    confirmButton = {
                        TextButton(onClick = { showErrorDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }



            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showSuccessDialog = false },
                    title = { Text("Registro exitoso") },
                    text = { Text("Se ha enviado un email con un link para finalizar con su registro.") },
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

            // Enlace a login
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Ya tengo cuenta, iniciar sesión", color = azulApp, fontSize = 18.sp)
            }
        }
    }

}
