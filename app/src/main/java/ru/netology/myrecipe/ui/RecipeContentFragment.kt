package ru.netology.myrecipe.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.myrecipe.R
import ru.netology.myrecipe.adapter.StepsAdapter
import ru.netology.myrecipe.data.Recipe
import ru.netology.myrecipe.data.RecipeRepository
import ru.netology.myrecipe.data.Step
import ru.netology.myrecipe.databinding.RecipeContentFragmentBinding
import ru.netology.myrecipe.viewModel.RecipeViewModel


class RecipeContentFragment : Fragment() {


    private val args by navArgs<RecipeContentFragmentArgs>()
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var currentRecipe: Recipe

    private val gson = Gson()
    private val prefs = context?.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Array<Step>::class.java).type
    private val key = "steps"
    var steps = emptyList<Step>()
    val data = MutableLiveData(steps)

    init {
        prefs?.getString(key, null)?.let {
            steps = gson.fromJson(it, type)
            data.value = steps
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater).also { binding ->

        //val initialSteps = Gson().fromJson(args.initialSteps, Array<Step>::class.java).asList()

        val adapter = StepsAdapter(viewModel)
        //binding.stepsRecyclerView.adapter = adapter

        adapter.submitList(steps)

        with(binding) {
            titleRecipe.setText(args.initialTitle)
            //titleRecipe.focusAndShowKeyboard()
            editCategoryRecipe.setText(args.initialCategory)

        }
        //binding.imageStep.setImageURI()

        binding.saveRecipeButton.setOnClickListener {
            onSaveButtonClicked(binding)
            findNavController().navigate(R.id.feed_fragment)
        }

        binding.addStepButton.setOnClickListener {
            val direction =
                RecipeContentFragmentDirections.actionRecipeContentFragmentToStepContentFragment()
            findNavController().navigate(direction)
        }

        setFragmentResultListener(
            requestKey = "newStepKey"
        ) { requestKey, bundle ->
            if (requestKey != "newStepKey") return@setFragmentResultListener
            val newStepText =
                bundle.getString("newStepText") ?: return@setFragmentResultListener
            val newStep = Step(RecipeRepository.NEW_STEP_ID, newStepText, currentRecipe.id)
            binding.titleRecipe.setText(args.initialTitle)
            binding.editCategoryRecipe.setText(args.initialCategory)
           // currentRecipe.steps = currentRecipe.steps?.plus(newStep)
            viewModel.currentRecipe.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.submitList(it.steps)
                }
            }
        }
    }.root

    //  private var dataSteps: List<String> = args.initialSteps?.split("&").orEmpty()

    //private var dataSteps = steps ?: emptyList()
    //if (!steps.isNullOrEmpty()) steps else emptyList()

//    private fun onAddStepButtonClicked(binding: RecipeContentFragmentBinding) {
//
//        val direction =
//            RecipeContentFragmentDirections.actionRecipeContentFragmentToStepContentFragment()
//        findNavController().navigate(direction)


//        val stepText = binding.editStepsRecipe.text.toString()
//        if (stepText.isBlank()) return
//        dataSteps = dataSteps.plus(Step(0, stepText)) as MutableList<Step>
//
//        val recyclerView: RecyclerView = requireView().findViewById(R.id.stepsRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = StepsAdapter(dataSteps)
//
//        binding.stepsRecyclerView.adapter = StepsAdapter(dataSteps)
//        binding.editStepsRecipe.text?.clear()


    //dataSteps.joinToString("&")
//    }


    private fun onSaveButtonClicked(binding: RecipeContentFragmentBinding) {

        val adapter = StepsAdapter(viewModel)
        binding.stepsRecyclerView.adapter = adapter

        val title = binding.titleRecipe.text.toString()
        val category = binding.editCategoryRecipe.text.toString()


        val steps: String? = gson.toJson(currentRecipe.steps)//currentRecipe.steps

        if (title.isNotBlank()) //&& steps.isNotEmpty())
        {
//            viewModel.onSaveButtonClicked(
//                title = title.toString(),
//                category = category.toString(),
//                steps = currentRecipe.steps
//            )
            val resultBundle = Bundle()
            resultBundle.putString("newRecipeTitle", title)
            resultBundle.putString("newRecipeCategory", category)
            resultBundle.putString("newRecipeSteps", steps)

            setFragmentResult("requestKey", resultBundle)
        }
        findNavController().popBackStack()
    }

}



