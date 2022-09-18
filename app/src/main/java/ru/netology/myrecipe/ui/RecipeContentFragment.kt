package ru.netology.myrecipe.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipe.R
import ru.netology.myrecipe.adapter.StepsAdapter
import ru.netology.myrecipe.databinding.RecipeContentFragmentBinding


class RecipeContentFragment : Fragment() {


    private val args by navArgs<RecipeContentFragmentArgs>()

    private var dataSteps: MutableList<String> =
        //args.initialSteps?.split("&")?.toMutableList() ?:
        emptyList<String>().toMutableList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater).also { binding ->

        val initialSteps = args.initialSteps?.split("&")
        val adapter = initialSteps?.let { StepsAdapter(it) }
        binding.stepsRecyclerView.adapter = adapter


        with(binding) {
            titleRecipe.setText(args.initialTitle)
            //TODO вернуть titleRecipe.focusAndShowKeyboard()
            editCategoryRecipe.setText(args.initialCategory)
            //editStepsRecipe.setText(args.initialSteps)
        }
        //binding.imageStep.setImageURI()

        binding.saveRecipeButton.setOnClickListener {
            onSaveButtonClicked(binding)
            findNavController().navigate(R.id.feed_fragment)
        }

        binding.addStepButton.setOnClickListener {
            onAddStepButtonClicked(binding)
        }

    }.root

    //  private var dataSteps: List<String> = args.initialSteps?.split("&").orEmpty()

    //private var dataSteps = steps ?: emptyList()
    //if (!steps.isNullOrEmpty()) steps else emptyList()

    private fun onAddStepButtonClicked(binding: RecipeContentFragmentBinding) {

        val stepText = binding.editStepsRecipe.text.toString()
        if (stepText.isBlank()) return
        dataSteps = dataSteps.plus(stepText) as MutableList<String>

        val recyclerView: RecyclerView = requireView().findViewById(R.id.stepsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = StepsAdapter(dataSteps)

        binding.stepsRecyclerView.adapter = StepsAdapter(dataSteps)
        binding.editStepsRecipe.text?.clear()


        //dataSteps.joinToString("&")
    }


    private fun onSaveButtonClicked(binding: RecipeContentFragmentBinding) {

        val adapter = StepsAdapter(dataSteps)
        binding.stepsRecyclerView.adapter = adapter


        val title = binding.titleRecipe.text
        val category = binding.editCategoryRecipe.text
        val steps = adapter.getItems().joinToString("&")
        //val steps = dataSteps.joinToString("&")
        val pictureUrl = ""


        if (!title.isNullOrBlank() && steps.isNotEmpty()) {
            val resultBundle = Bundle(4)
            resultBundle.putString("newRecipeTitle", title.toString())
            resultBundle.putString("newRecipeCategory", category.toString())
            resultBundle.putString("newRecipeSteps", steps.toString())
            resultBundle.putString("newPictureUrl", pictureUrl)

            setFragmentResult("requestKey", resultBundle)
        }
        findNavController().popBackStack()
    }
}



