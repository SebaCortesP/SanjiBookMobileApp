package com.duocuc.sanjibookapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duocuc.sanjibookapp.data.dao.RecipeDao
import com.duocuc.sanjibookapp.data.dao.RoleDao
import com.duocuc.sanjibookapp.data.dao.UserDao
import com.duocuc.sanjibookapp.models.User
import com.duocuc.sanjibookapp.models.Role
import com.duocuc.sanjibookapp.models.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [User::class, Role::class, Recipe::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun roleDao(): RoleDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sanjibook_db"
                )
                    .addCallback(AppDatabaseCallback(context)) // callback para datos iniciales
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Se ejecuta solo cuando la DB se crea por primera vez
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                val roleDao = database.roleDao()
                val userDao = database.userDao()
                val recipeDao = database.recipeDao()

                // Insertar 3 roles de prueba
                val roles = listOf(
                    Role(id = 0, name = "Administrador"),
                    Role(id = 0, name = "Editor"),
                    Role(id = 0, name = "Usuario")
                )
                roles.forEach { roleDao.insert(it) }

                // Insertar 5 usuarios de prueba (asignando roleId de ejemplo)
                val users = listOf(
                    User(id = 0, nombre = "Sebastián", email = "sebastian@demo.com", password = "1234", roleId = 3),
                    User(id = 0, nombre = "Ana", email = "ana@demo.com", password = "1234", roleId = 3),
                    User(id = 0, nombre = "Carlos", email = "carlos@demo.com", password = "1234", roleId = 3),
                    User(id = 0, nombre = "Laura", email = "laura@demo.com", password = "1234", roleId = 3),
                    User(id = 0, nombre = "Pedro", email = "pedro@demo.com", password = "1234", roleId = 3)
                )

                users.forEach { userDao.insert(it) }

                // Insertar recetas
                val recipes = listOf(
                    Recipe(id = 0, name = "Ensalada César", ingredients = listOf("Lechuga", "Pollo", "Queso"), preparation = "Mezclar todo", category = "Ensaladas", userId = 1),
                    Recipe(id = 0, name = "Pasta al pesto", ingredients = listOf("Pasta", "Pesto", "Queso"), preparation = "Cocinar pasta y mezclar con pesto", category = "Pastas", userId = 2)
                )
                recipes.forEach { recipeDao.insert(it) }
            }
        }
    }
}


