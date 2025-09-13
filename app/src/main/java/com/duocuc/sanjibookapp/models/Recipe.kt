package com.duocuc.sanjibookapp.models

data class Recipe(
    var name: String,
    var ingredients: List<String>,
    var preparation: String,
    var category: String,
    var owner: User
) {
    companion object {
        // Lista mutable de recetas
        private val recipes = mutableListOf<Recipe>()

        // Dummy user para las recetas iniciales
        private val dummyUser = User("user1@mail.com", "123456")

        // Inicializar recetas dummy al cargar la app
        init {
            val dummyRecipes = listOf(
                Recipe(
                    name = "Ensalada César",
                    ingredients = listOf("Lechuga", "Pollo", "Queso parmesano", "Crutones", "Aderezo César"),
                    preparation = "Mezclar todos los ingredientes en un bol y añadir aderezo al gusto.",
                    category = "Ensalada",
                    owner = dummyUser
                ),
                Recipe(
                    name = "Pollo al horno",
                    ingredients = listOf("Pollo", "Aceite de oliva", "Sal", "Pimienta", "Hierbas provenzales"),
                    preparation = "Sazonar el pollo y hornear a 200°C durante 40 minutos.",
                    category = "Plato principal",
                    owner = dummyUser
                ),
                Recipe(
                    name = "Smoothie de frutas",
                    ingredients = listOf("Plátano", "Fresas", "Leche", "Miel"),
                    preparation = "Licuar todos los ingredientes hasta obtener una mezcla homogénea.",
                    category = "Bebida",
                    owner = dummyUser
                ),
                Recipe(
                    name = "Pasta boloñesa",
                    ingredients = listOf("Pasta", "Carne molida", "Tomate triturado", "Cebolla", "Ajo", "Aceite"),
                    preparation = "Cocinar la pasta y preparar la salsa con los demás ingredientes, luego mezclar.",
                    category = "Plato principal",
                    owner = dummyUser
                ),
                Recipe(
                    name = "Sopa de verduras",
                    ingredients = listOf("Zanahoria", "Apio", "Cebolla", "Papas", "Caldo de verduras"),
                    preparation = "Cortar todas las verduras y hervir en el caldo hasta que estén tiernas.",
                    category = "Sopa",
                    owner = dummyUser
                )
            )
            recipes.addAll(dummyRecipes)
        }

        // Listar todas las recetas
        fun getAll(): List<Recipe> = recipes.toList()

        // Listar solo las recetas de un usuario
        fun getByOwner(user: User): List<Recipe> =
            recipes.filter { it.owner.email == user.email }

        // Agregar receta
        fun add(recipe: Recipe) {
            recipes.add(recipe)
        }

        // Editar receta por índice
        fun update(index: Int, recipe: Recipe) {
            if (index in recipes.indices) {
                recipes[index] = recipe
            }
        }

        // Buscar receta por nombre
        fun findByName(name: String): Recipe? {
            return recipes.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}
