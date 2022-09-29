package ru.netology.myrecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.myrecipe.db.RecipeDao
import ru.netology.myrecipe.db.toEntity
import ru.netology.myrecipe.db.toRecipe


class RecipeRepositoryImpl(private val dao: RecipeDao) : RecipeRepository {


    override var data: LiveData<List<Recipe>> = dao.getAll().map { entities ->
        entities.map {
            it.toRecipe()
        }
    }

    override var dataFavorite = dao.getFavorite().map { entities ->
        entities.map { it.toRecipe() }
    }

    override fun getAll():
            LiveData<List<Recipe>> = data


    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) dao.insert(recipe.toEntity()) else
            dao.updateContentById(
                recipe.id,
                recipe.title,
                recipe.author,
                recipe.category
            )
    }


    override fun favorite(recipeId: Int) {
        dao.favoriteById(recipeId)
    }


    override fun delete(recipeId: Int) {
        dao.removeById(recipeId)
    }

    override fun searchDatabase(searchQuery: String) {
        data = dao.searchDatabase(searchQuery).map { entities ->
            entities.map { it.toRecipe() }
        }
    }

    override fun searchFavoriteDatabase(searchQuery: String) {
        dataFavorite = dao.searchFavoriteDatabase(searchQuery).map { entities ->
            entities.map { it.toRecipe() }
        }
    }


    override fun clearFilters() {
        data = dao.getAll().map { entities ->
            entities.map { it.toRecipe() }
        }
    }

    override fun deleteStep(step: Step) {
        dao.deleteStep(step.id)
    }

    override fun saveStep(step: Step) {
        if (step.id == RecipeRepository.NEW_STEP_ID) step.toEntity()?.let { dao.insertStep(it) }
        else step.text?.let {
            dao.updateStep(
                step.id,
                it,
                step.idRecipe
            )
        }
    }


    override fun searchByCategory(categories: ArrayList<String>) {
        data = dao.searchByCategory(categories).map { entities ->
            entities.map { it.toRecipe() }
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

