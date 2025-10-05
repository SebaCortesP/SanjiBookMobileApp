package com.duocuc.sanjibookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.duocuc.sanjibookapp.data.database.AppDatabase
import com.duocuc.sanjibookapp.ui.components.TopNavBar
import com.duocuc.sanjibookapp.ui.home.HomeScreen
import com.duocuc.sanjibookapp.ui.login.LoginScreen
import com.duocuc.sanjibookapp.ui.recovery.RecoveryScreen
import com.duocuc.sanjibookapp.ui.register.RegisterScreen
import com.duocuc.sanjibookapp.ui.theme.SanjibookappTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- Inicializar la base de datos para que se creen los datos de prueba ---
        val db = AppDatabase.getDatabase(this)

        setContent {
            SanjibookappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    // Detectar la ruta actual
                    val currentRoute = navController
                        .currentBackStackEntryFlow
                        .collectAsState(initial = navController.currentBackStackEntry)
                        .value
                        ?.destination
                        ?.route

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            DrawerContent(
                                currentRoute = currentRoute,
                                navController = navController,
                                closeDrawer = { scope.launch { drawerState.close() } }
                            )
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                TopNavBar(
                                    title = when (currentRoute) {
                                        "login" -> "Iniciar sesión"
                                        "recovery" -> "Recuperar contraseña"
                                        "register" -> "Crear cuenta"
                                        "home" -> "Inicio"
                                        else -> "SanjiBook"
                                    },
                                    onBurgerClick = { scope.launch { drawerState.open() } }
                                )
                            }
                        ) { paddingValues ->
                            NavHost(
                                navController = navController,
                                startDestination = "login",
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                composable("login") { LoginScreen(navController) }
                                composable("register") { RegisterScreen(navController) }
                                composable("recovery") { RecoveryScreen(navController) }
                                composable("home") { HomeScreen(navController) }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun DrawerContent(
    currentRoute: String?,
    navController: NavController,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Text("Menú", modifier = Modifier.padding(16.dp))

        if (currentRoute in listOf("login", "recovery", "register")) {
            NavigationDrawerItem(
                label = { Text("Login") },
                selected = currentRoute == "login",
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                    closeDrawer()
                }
            )
            NavigationDrawerItem(
                label = { Text("Recovery") },
                selected = currentRoute == "recovery",
                onClick = {
                    navController.navigate("recovery")
                    closeDrawer()
                }
            )
            NavigationDrawerItem(
                label = { Text("Register") },
                selected = currentRoute == "register",
                onClick = {
                    navController.navigate("register")
                    closeDrawer()
                }
            )
        } else if (currentRoute == "home") {
            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = true,
                onClick = { closeDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Cerrar sesión") },
                selected = false,
                onClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                    closeDrawer()
                }
            )
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SanjibookappTheme {
        Greeting("Android")
    }
}