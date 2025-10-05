package com.duocuc.sanjibookapp.data.dao

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.duocuc.sanjibookapp.data.database.AppDatabase
import com.duocuc.sanjibookapp.models.Recipe
import com.duocuc.sanjibookapp.viewmodel.RecipeViewModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE user_id = :userId")
    suspend fun getRecipesByUser(userId: Int): List<Recipe>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :name || '%'")
    suspend fun findByName(name: String): List<Recipe>
}

class RecipeViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(db.recipeDao()) as T
    }
}
