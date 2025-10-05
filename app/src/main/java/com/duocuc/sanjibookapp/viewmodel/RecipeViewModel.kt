package com.duocuc.sanjibookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.sanjibookapp.data.dao.RecipeDao
import com.duocuc.sanjibookapp.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    // Live data reactiva desde Room usando Flow
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    // Insertar receta
    fun insert(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.insert(recipe)
        }
    }

    // Actualizar receta
    fun update(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.update(recipe)
        }
    }

    // Eliminar receta
    fun delete(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.delete(recipe)
        }
    }
}
