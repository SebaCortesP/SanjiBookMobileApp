package com.duocuc.sanjibookapp.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.json.JSONArray

data class Recipe(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val json = """
        [
          {"name": "Ensalada César", "calories": 320, "protein": 15, "carbs": 25, "fat": 18},
          {"name": "Pollo al horno", "calories": 450, "protein": 40, "carbs": 10, "fat": 25},
          {"name": "Smoothie de frutas", "calories": 200, "protein": 5, "carbs": 45, "fat": 2},
          {"name": "Pasta boloñesa", "calories": 600, "protein": 30, "carbs": 70, "fat": 20},
          {"name": "Sopa de verduras", "calories": 150, "protein": 6, "carbs": 20, "fat": 3}
        ]
    """.trimIndent()

    val recipes = remember { parseRecipesFromJson(json) }
    var menuExpanded by remember { mutableStateOf(false) }

    val azulNavbar = Color(0xFF243B94)
    val azulMenu = Color(0xFF2d4396)

    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Bienvenido a MinutriApp", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(recipes) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Calorías: ${recipe.calories}")
                            Text("Proteínas: ${recipe.protein} g")
                            Text("Carbohidratos: ${recipe.carbs} g")
                            Text("Grasas: ${recipe.fat} g")
                        }
                    }
                }
            }
        }
    }
}

fun parseRecipesFromJson(json: String): List<Recipe> {
    val jsonArray = JSONArray(json)
    val recipes = mutableListOf<Recipe>()
    for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        recipes.add(
            Recipe(
                name = obj.getString("name"),
                calories = obj.getInt("calories"),
                protein = obj.getInt("protein"),
                carbs = obj.getInt("carbs"),
                fat = obj.getInt("fat")
            )
        )
    }
    return recipes
}