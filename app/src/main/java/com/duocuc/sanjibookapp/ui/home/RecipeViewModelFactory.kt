package com.duocuc.sanjibookapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duocuc.sanjibookapp.data.database.AppDatabase
import com.duocuc.sanjibookapp.viewmodel.RecipeViewModel

class RecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)
        val recipeDao = db.recipeDao()

        @Suppress("UNCHECKED_CAST")
        return RecipeViewModel(recipeDao) as T
    }
}