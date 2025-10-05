package com.duocuc.sanjibookapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duocuc.sanjibookapp.models.Recipe
import com.duocuc.sanjibookapp.models.Session
import com.duocuc.sanjibookapp.models.User
import com.duocuc.sanjibookapp.viewmodel.RecipeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val currentUser = Session.currentUser ?: return

    // Usamos el ViewModel con Room
    val recipeViewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModelFactory(context)
    )

    // Escuchamos los cambios de la base de datos en tiempo real
    val recipes by recipeViewModel.allRecipes.collectAsState(initial = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }
    var recipeToEdit by remember { mutableStateOf<Recipe?>(null) }

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
                itemsIndexed(recipes) { _, recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                            Text("Categoría: ${recipe.category}")
                            Text("Autor ID: ${recipe.userId}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Ingredientes: ${recipe.ingredients.joinToString(", ")}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Preparación: ${recipe.preparation}")
                            Spacer(modifier = Modifier.height(8.dp))

                            // Solo mostrar botones si es dueño o administrador
                            if (recipe.userId == currentUser.id || currentUser.roleId == 1) {
                                Row {
                                    Button(
                                        onClick = { recipeToEdit = recipe },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Button(
                                        onClick = { recipeViewModel.delete(recipe) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Eliminar", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Dialog Agregar o Editar Receta ---
    if (showAddDialog || recipeToEdit != null) {
        RecipeDialog(
            user = currentUser,
            recipe = recipeToEdit,
            onDismiss = {
                showAddDialog = false
                recipeToEdit = null
            },
            onSave = { recipe ->
                if (recipeToEdit != null) {
                    recipeViewModel.update(recipe)
                } else {
                    recipeViewModel.insert(recipe)
                }
                showAddDialog = false
                recipeToEdit = null
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

    // Convertir lista de ingredientes a texto para editar
    var ingredientsText by remember {
        mutableStateOf(recipe?.ingredients?.joinToString(", ") ?: "")
    }

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
                OutlinedTextField(
                    value = ingredientsText,
                    onValueChange = { ingredientsText = it },
                    label = { Text("Ingredientes (separados por coma)") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = preparation, onValueChange = { preparation = it }, label = { Text("Preparación") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val ingredientsList = ingredientsText
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                val newRecipe = Recipe(
                    id = recipe?.id ?: 0,
                    name = name,
                    category = category,
                    ingredients = ingredientsList, // 👈 ahora sí es List<String>
                    preparation = preparation,
                    userId = user.id
                )
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

