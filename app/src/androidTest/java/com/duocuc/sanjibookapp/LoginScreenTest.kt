package com.duocuc.sanjibookapp.ui.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.duocuc.sanjibookapp.data.database.AppDatabase
import com.duocuc.sanjibookapp.models.Role
import com.duocuc.sanjibookapp.models.User
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<android.content.Context>()
            db = AppDatabase.getDatabase(context) // ver siguiente punto
            val roleDao = db.roleDao()
            val userDao = db.userDao()

            roleDao.insert(Role(1, "Administrador"))
            roleDao.insert(Role(3, "Usuario"))
            userDao.insert(User(0, "admin@demo.com", "admin123", "Admin", "user", "", "", true, 1))
        }
    }


    @Test
    fun loginScreen_displaysFieldsAndButton() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            LoginScreen(navController)
        }

        // Verificar campos visibles
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsErrorWhenEmptyFields() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            LoginScreen(navController)
        }

        // Click en botón sin ingresar datos
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        // Verificar errores
        composeTestRule.onNodeWithText("El email es obligatorio").assertIsDisplayed()
        composeTestRule.onNodeWithText("La contraseña es obligatoria").assertIsDisplayed()
    }
}
