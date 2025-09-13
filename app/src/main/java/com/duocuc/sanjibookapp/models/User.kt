package com.duocuc.sanjibookapp.models

import android.util.Patterns
import com.duocuc.sanjibookapp.interfaces.Validable
data class User(
    val email: String,
    val password: String,
    val nombre: String = "",
    val apellido: String = "",
    val repetirPassword: String = "",
    val fechaNacimiento: String = "",
    val sexo: String = "",
    val terminosAceptados: Boolean = false
) : Validable {
    override fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun isValidPassword(): Boolean {
        return password.isNotBlank() && password.length >= 6
    }

    /**
     * Valida que el usuario tenga email y password correctos
     */
    override fun validate(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()

        if (email.isBlank()) {
            errors["email"] = "El email es obligatorio"
        } else if (!isValidEmail()) {
            errors["email"] = "Formato de email inválido"
        }

        if (password.isBlank()) {
            errors["password"] = "La contraseña es obligatoria"
        }

        return errors
    }

    /**
     * Retorna true si el login fue exitoso,
     * y setea loginError si falló.
     */
    fun authenticate(): Boolean {
        val user = allowedUsers.find { it.first == email }

        return when {
            user == null -> {
                lastError = "El usuario no existe"
                false
            }
            user.second != password -> {
                lastError = "Contraseña incorrecta"
                false
            }
            else -> {
                lastError = null
                true
            }
        }
    }

    fun validateRegistration(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()

        if (nombre.isBlank()) errors["nombre"] = "Obligatorio"
        if (apellido.isBlank()) errors["apellido"] = "Obligatorio"

        if (email.isBlank()) errors["email"] = "El email es obligatorio"
        else if (!isValidEmail()) errors["email"] = "Formato de email inválido"

        if (password.isBlank() || repetirPassword.isBlank()) errors["password"] = "Ambas contraseñas son obligatorias"
        else if (password != repetirPassword) errors["password"] = "Las contraseñas no coinciden"

        if (fechaNacimiento.isBlank()) errors["fechaNacimiento"] = "Obligatorio"
        else {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).apply { isLenient = false }
            try {
                val dob = sdf.parse(fechaNacimiento)
                val limite = java.util.Calendar.getInstance().apply { add(java.util.Calendar.YEAR, -18) }
                if (dob == null || !dob.before(limite.time)) errors["fechaNacimiento"] = "Debes ser mayor de 18 años"
            } catch (e: java.text.ParseException) {
                errors["fechaNacimiento"] = "Fecha inválida"
            }
        }

        if (sexo.isBlank()) errors["sexo"] = "Obligatorio"
        if (!terminosAceptados) errors["terminos"] = "Debes aceptar los términos"

        return errors
    }



    companion object {
        private val allowedUsers = mutableListOf(
            "user1@mail.com" to "123456",
            "user2@mail.com" to "123456",
            "user3@mail.com" to "123456",
            "user4@mail.com" to "123456",
            "user5@mail.com" to "123456"
        )

        var lastError: String? = null
            private set

        fun registerUser(user: User): Boolean {
            if (allowedUsers.any { it.first == user.email }) {
                lastError = "El usuario ya existe"
                return false
            }
            allowedUsers.add(user.email to user.password)
            lastError = null
            return true
        }

        fun login(email: String, password: String): Boolean {
            val user = allowedUsers.find { it.first == email }
            return when {
                user == null -> {
                    lastError = "El usuario no existe"
                    false
                }
                user.second != password -> {
                    lastError = "Contraseña incorrecta"
                    false
                }
                else -> {
                    lastError = null
                    true
                }
            }
        }
    }



}
