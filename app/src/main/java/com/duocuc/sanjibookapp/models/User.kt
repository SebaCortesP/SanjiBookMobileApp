package com.duocuc.sanjibookapp.models

import android.util.Patterns
import androidx.room.*
import com.duocuc.sanjibookapp.interfaces.Validable
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "users",
    foreignKeys = [ForeignKey(
        entity = Role::class,
        parentColumns = ["id"],
        childColumns = ["role_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("role_id")]           // Índice en la FK
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val email: String,
    val password: String,
    val nombre: String = "",
    val apellido: String = "",
    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String = "",
    val sexo: String = "",
    @ColumnInfo(name = "terminos_aceptados")
    val terminosAceptados: Boolean = false,
    @ColumnInfo(name = "role_id")
    val roleId: Int = 1 //default 1, user
) : Validable {

    // ==========================
    // Validaciones
    // ==========================

    override fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun isValidPassword(): Boolean {
        return password.isNotBlank() && password.length >= 6
    }

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

    fun validateRegistration(repetirPassword: String): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()

        if (nombre.isBlank()) errors["nombre"] = "Obligatorio"
        if (apellido.isBlank()) errors["apellido"] = "Obligatorio"

        if (email.isBlank()) errors["email"] = "El email es obligatorio"
        else if (!isValidEmail()) errors["email"] = "Formato de email inválido"

        if (password.isBlank() || repetirPassword.isBlank()) errors["password"] = "Ambas contraseñas son obligatorias"
        else if (password != repetirPassword) errors["password"] = "Las contraseñas no coinciden"

        if (fechaNacimiento.isBlank()) errors["fechaNacimiento"] = "Obligatorio"
        else {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { isLenient = false }
            try {
                val dob = sdf.parse(fechaNacimiento)
                val limite = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }
                if (dob == null || !dob.before(limite.time)) errors["fechaNacimiento"] = "Debes ser mayor de 18 años"
            } catch (e: java.text.ParseException) {
                errors["fechaNacimiento"] = "Fecha inválida"
            }
        }

        if (sexo.isBlank()) errors["sexo"] = "Obligatorio"
        if (!terminosAceptados) errors["terminos"] = "Debes aceptar los términos"

        return errors
    }
}
