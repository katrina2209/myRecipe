package ru.netology.myrecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.myrecipe.adapter.RecipeInteractionListener
import ru.netology.myrecipe.adapter.StepInteractionListener
import ru.netology.myrecipe.data.*
import ru.netology.myrecipe.db.AppDb
import ru.netology.myrecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener, StepInteractionListener {

    private val repository: RecipeRepository =
        RecipeRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
        )

    val data get() = repository.data

    val currentRecipe = MutableLiveData<Recipe?>(null)
    private val currentStep = MutableLiveData<Step?>(null)

    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe>()
    val navigateToRecipeCardFragmentEvent = SingleLiveEvent<Int>()
    val stepEditScreenEvent = SingleLiveEvent<Step>()
    val stepAddScreenEvent = SingleLiveEvent<String>()


    fun onSaveButtonClicked(
        title: String, category: String, steps: List<Step?>
    ) {

        if (title.isBlank()) return
        //if (category.isBlank()) return
        //if (steps.isEmpty()) return
        val recipe =
            currentRecipe.value?.copy(
                title = title,
                category = category,
                steps = steps
//                steps = steps?.map {
//                    Step(RecipeRepository.NEW_STEP_ID, it, currentRecipe.value!!.id)
//                },

                ) ?: Recipe(
                id = RecipeRepository.NEW_RECIPE_ID,
                author = "Me",
                title = title,
                category = category,
                steps = steps
//                steps = steps?.map {
//                    Step(RecipeRepository.NEW_STEP_ID, it, currentRecipe.value!!.id)
//                }
            )
        repository.save(recipe)
        currentRecipe.value = null
    }

    fun onSaveStepButtonClicked(text: String) {
        if (text.isBlank()) return
        val step = currentStep.value?.copy(
            text = text,
        ) ?: Step(
            id = RecipeRepository.NEW_STEP_ID,
            idRecipe = currentRecipe.value?.id ?: 0,
            text = text,
        )
        repository.saveStep(step)
        currentStep.value = null
        currentRecipe.value = null
    }

    fun onAddStepClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        stepAddScreenEvent.call()
    }



    fun searchDatabase(searchQuery: String) = repository.searchDatabase(searchQuery)

    fun filterByCategory(categories: ArrayList<String>) {
        repository.searchByCategory(categories)

    }

    fun clearFilters() {
        repository.clearFilters()
        //println(repository.data.value)
        //  repository.data.value
        // repository.searchByCategory(ArrayList())
    }

// region RecipeInteractionListener

    override fun onFavoriteClicked(recipe: Recipe) {
        repository.favorite(recipe.id)

    }

    override fun onRemoveClicked(recipe: Recipe) {
        currentRecipe.value = null
        repository.delete(recipe.id)
    }

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value = recipe

    }

    override fun onRecipeClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeCardFragmentEvent.value = recipe.id

    }

    // endregion RecipeInteractionListener

    override fun onRemoveStepClicked(step: Step) = repository.deleteStep(step)

    override fun onEditStepClicked(step: Step) {
        currentStep.value = step
        stepEditScreenEvent.value = step
    }

}

