package ru.netology.myrecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.myrecipe.db.RecipeDao
import ru.netology.myrecipe.db.toEntity
import ru.netology.myrecipe.db.toModel

class RecipeRepositoryImpl(private val dao: RecipeDao) : RecipeRepository {


    override var data: LiveData<List<Recipe>> = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override var dataFavorite = dao.getFavorite().map { entities ->
        entities.map { it.toModel() }
    }

    override fun getAll(): LiveData<List<Recipe>> = data


    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) dao.insert(recipe.toEntity()) else

            recipe.pictureUrl?.let {
                dao.updateContentById(
                    recipe.id,
                    recipe.title,
                    recipe.author,
                    recipe.category,
                    recipe.steps,
                    it
                )
            }
    }


    override fun favorite(recipeId: Int) {
        dao.favoriteById(recipeId)
    }


    override fun delete(recipeId: Int) {
        dao.removeById(recipeId)
    }

    override fun searchDatabase(searchQuery: String) {
        data = dao.searchDatabase(searchQuery).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun searchFavoriteDatabase(searchQuery: String) {
        dataFavorite = dao.searchFavoriteDatabase(searchQuery).map { entities ->
            entities.map { it.toModel() }
        }
    }

    //override fun filterByCategory(categories: ArrayList<String>) {


    override fun searchByCategory(categories: ArrayList<String>) {
        data = dao.searchByCategory(categories).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun filterByCategory(categories: ArrayList<String>) {
        data = if (categories.isEmpty()) data
        else {
            val filteredData = data.map { list ->
                val newRecipesList = list.filter {
                    it.category in categories
                }
                newRecipesList
            }
            filteredData
        }
    }

}

