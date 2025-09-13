package com.duocuc.sanjibookapp.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duocuc.sanjibookapp.models.Recipe
import com.duocuc.sanjibookapp.models.Session
import com.duocuc.sanjibookapp.models.User
import org.json.JSONArray
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val currentUser = Session.currentUser ?: return // o mostrar un error si no hay user
    var recipes by remember { mutableStateOf(Recipe.getAll()) }

    // Estados para dialogs
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Pair<Boolean, Int>>(false to -1) }

    val azulNavbar = Color(0xFF243B94)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                itemsIndexed(recipes) { index, recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                            Text("Categoría: ${recipe.category}")
                            Text("Autor: ${recipe.owner.email}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Ingredientes: ${recipe.ingredients.joinToString(", ")}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Preparación: ${recipe.preparation}")
                            Spacer(modifier = Modifier.height(8.dp))
                            // Botón Editar
                            Button(onClick = { showEditDialog = true to index }) {
                                Text("Editar")
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Dialog Agregar Receta ---
    if (showAddDialog) {
        RecipeDialog(
            user = currentUser,
            onDismiss = { showAddDialog = false },
            onSave = { newRecipe ->
                Recipe.add(newRecipe)
                recipes = Recipe.getAll()
                showAddDialog = false
            }
        )
    }

    // --- Dialog Editar Receta ---
    if (showEditDialog.first) {
        val recipeToEdit = recipes[showEditDialog.second]
        RecipeDialog(
            recipe = recipeToEdit,
            user = currentUser,
            onDismiss = { showEditDialog = false to -1 },
            onSave = { updatedRecipe ->
                Recipe.update(showEditDialog.second, updatedRecipe)
                recipes = Recipe.getAll()
                showEditDialog = false to -1
            }
        )
    }
}

@Composable
fun RecipeDialog(
    user: User,
    recipe: Recipe? = null,
    onDismiss: () -> Unit,
    onSave: (Recipe) -> Unit
) {
    var name by remember { mutableStateOf(recipe?.name ?: "") }
    var category by remember { mutableStateOf(recipe?.category ?: "") }
    var ingredientsText by remember { mutableStateOf(recipe?.ingredients?.joinToString(", ") ?: "") }
    var preparation by remember { mutableStateOf(recipe?.preparation ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (recipe == null) "Agregar Receta" else "Editar Receta") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = ingredientsText, onValueChange = { ingredientsText = it }, label = { Text("Ingredientes (separados por coma)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = preparation, onValueChange = { preparation = it }, label = { Text("Preparación") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val ingredients = ingredientsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val newRecipe = Recipe(name, ingredients, preparation, category, user)
                onSave(newRecipe)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
