package ru.netology.myrecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    val dataFavorite: LiveData<List<Recipe>>

    fun getAll(): LiveData<List<Recipe>>

    fun favorite(recipeId: Int)

    fun delete(recipeId: Int)

    fun save(recipe: Recipe)

    fun searchDatabase(searchQuery: String)

    fun searchFavoriteDatabase(searchQuery: String)

    fun filterByCategory(categories: ArrayList<String>)

    fun searchByCategory(categories: ArrayList<String>)

    fun clearFilters()

    fun deleteStep(step: Step)

    fun saveStep(step: Step)


    companion object {
        const val NEW_RECIPE_ID = 0
        const val NEW_STEP_ID = 0
    }
}