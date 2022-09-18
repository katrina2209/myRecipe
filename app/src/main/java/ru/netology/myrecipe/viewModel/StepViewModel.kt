package ru.netology.myrecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.myrecipe.adapter.StepInteractionListener
import ru.netology.myrecipe.data.Recipe
import ru.netology.myrecipe.data.RecipeRepository
import ru.netology.myrecipe.data.RecipeRepositoryImpl
import ru.netology.myrecipe.db.AppDb

class StepViewModel(
    application: Application
) : AndroidViewModel(application), StepInteractionListener {

    private val repository: RecipeRepository =
        RecipeRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
        )

    val data get() = repository.data


//    val navigateToRecipeContentScreenEvent = SingleLiveEvent<EditRecipeResult?>()
//    val navigateToRecipeCardFragmentEvent = SingleLiveEvent<Int>()

    private val currentRecipe = MutableLiveData<Recipe?>(null)
    private val currentStep = MutableLiveData<String?>(null)

//    fun onSaveButtonClicked(
//        title: String, category: String, steps: String, pictureUrl: String?
//    ) {
//
//        if (title.isBlank()) return
//        //if (category.isBlank()) return
//        if (steps.isEmpty()) return
//        val recipe =
//            currentRecipe.value?.copy(
//                title = title,
//                category = category,
//                steps = steps,
//                pictureUrl = pictureUrl
//            ) ?: Recipe(
//                id = RecipeRepository.NEW_RECIPE_ID,
//                author = "Me",
//                title = title,
//                category = category,
//                steps = steps,
//                pictureUrl = pictureUrl
//            )
//        repository.save(recipe)
//        currentRecipe.value = null
//    }


// region RecipeInteractionListener


    override fun onRemoveStepClicked(step: String) {
        currentStep.value = null
       // repository.save(currentRecipe)
    }

    override fun onEditStepClicked(step: String) {
        currentStep.value = step
    }

    // endregion RecipeInteractionListener


}

