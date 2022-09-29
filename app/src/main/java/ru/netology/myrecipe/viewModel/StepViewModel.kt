package ru.netology.myrecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.myrecipe.adapter.StepInteractionListener
import ru.netology.myrecipe.data.Recipe
import ru.netology.myrecipe.data.RecipeRepository
import ru.netology.myrecipe.data.RecipeRepositoryImpl
import ru.netology.myrecipe.data.Step
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
    private val currentStep = MutableLiveData<Step?>(null)

    fun onAddStepButtonClicked(
        text: String
    ) {
        if (text.isBlank()) return

        val step =
            currentStep.value?.copy(
                text = text
            ) ?: currentRecipe.value?.let {
                Step(
                    id = RecipeRepository.NEW_STEP_ID,
                    text = text,
                    it.id
                )
            }
        //currentRecipe.value?.steps?.plusAssign(step)

        //currentRecipe.value = null
    }


// region RecipeInteractionListener


    override fun onRemoveStepClicked(step: Step) {
        currentStep.value = null
        // repository.save(currentRecipe)
    }

    override fun onEditStepClicked(step: Step) {
        currentStep.value = step
    }

    // endregion RecipeInteractionListener


}

