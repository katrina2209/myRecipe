package ru.netology.myrecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myrecipe.adapter.RecipeInteractionListener
import ru.netology.myrecipe.data.EditRecipeResult
import ru.netology.myrecipe.data.Recipe
import ru.netology.myrecipe.data.RecipeRepository
import ru.netology.myrecipe.data.RecipeRepositoryImpl
import ru.netology.myrecipe.db.AppDb
import ru.netology.myrecipe.util.SingleLiveEvent

class FavoriteViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: RecipeRepository =
        RecipeRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
        )

    val dataFavorite get() = repository.dataFavorite

    val navigateToRecipeContentScreenEvent = SingleLiveEvent<EditRecipeResult?>()
    val navigateToRecipeCardFragmentEvent = SingleLiveEvent<Int>()


    private val currentRecipe = MutableLiveData<Recipe?>(null)


    fun onSaveButtonClicked(
        title: String, category: String, steps: String, pictureUrl: String?
    ) {

        if (title.isBlank()) return
        val recipe =
            currentRecipe.value?.copy(
                title = title,
                category = category,
                steps = steps,
                pictureUrl = pictureUrl
            ) ?: Recipe(
                id = RecipeRepository.NEW_RECIPE_ID,
                author = "Me",
                title = title,
                category = category,
                steps = steps,
                pictureUrl = pictureUrl
            )
        repository.save(recipe)
        currentRecipe.value = null
    }

    fun searchDatabase(searchQuery: String) = repository.searchDatabase(searchQuery)

    fun searchFavoriteDataBase (searchQuery: String) = repository.searchFavoriteDatabase(searchQuery)


    // region RecipeInteractionListener

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)


    override fun onRemoveClicked(recipe: Recipe) {
        currentRecipe.value = null
        repository.delete(recipe.id)
    }

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value =
            EditRecipeResult(recipe.title, recipe.category, recipe.steps, recipe.pictureUrl)
    }

    override fun onRecipeClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeCardFragmentEvent.value = recipe.id

    }
    // endregion RecipeInteractionListener
}

