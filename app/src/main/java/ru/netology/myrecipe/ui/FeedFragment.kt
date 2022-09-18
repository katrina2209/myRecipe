package ru.netology.myrecipe.ui


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myrecipe.adapter.RecipesAdapter
import ru.netology.myrecipe.databinding.FeedFragmentBinding
import ru.netology.myrecipe.viewModel.RecipeViewModel


class FeedFragment : Fragment() {

    private val viewModel: RecipeViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(
            requestKey = "requestKey"
        ) { requestKey, bundle ->
            if (requestKey != "requestKey") return@setFragmentResultListener
            val newRecipeTitle =
                bundle.getString("newRecipeTitle") ?: return@setFragmentResultListener
            val newRecipeCategory =
                bundle.getString("newRecipeCategory")
            val newRecipeSteps = bundle.getString("newRecipeSteps") //?.split("&")
            val newPictureUrl = bundle.getString("newPictureUrl")

            viewModel.onSaveButtonClicked(
                newRecipeTitle,
                newRecipeCategory.orEmpty(),
                newRecipeSteps.orEmpty(),
                newPictureUrl
            )
        }


        setFragmentResultListener(
            requestKey = "chosenCategories"
        ) { requestKey, bundle ->
            if (requestKey != "chosenCategories") return@setFragmentResultListener

            val chosenCategories = bundle.getStringArrayList("listOfChosenCategories")
            if (chosenCategories != null) {
                viewModel.filterByCategory(chosenCategories)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->

            if (recipes.isNullOrEmpty()) {
                binding.emptyDataPic.visibility = VISIBLE
                binding.emptyText.visibility = VISIBLE
                binding.recipesRecyclerView.visibility = GONE
                binding.buttonToFilters.visibility = GONE
                binding.searchView.visibility = GONE

            } else {
                binding.recipesRecyclerView.visibility = VISIBLE
                binding.recipesRecyclerView.scrollToPosition(0)
                binding.emptyDataPic.visibility = GONE
                binding.emptyText.visibility = GONE
                binding.buttonToFilters.visibility = VISIBLE
                binding.searchView.visibility = VISIBLE
                adapter.submitList(recipes)
            }
        }




        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { editRecipeResult ->
            val direction =
                FeedFragmentDirections.actionFeedFragmentToRecipeContentFragment(
                    editRecipeResult?.newTitle,
                    editRecipeResult?.newCategory,
                    editRecipeResult?.newSteps, //?.joinToString("&"),
                    editRecipeResult?.pictureUrl
                )
            findNavController().navigate(direction)
        }


        viewModel.navigateToRecipeCardFragmentEvent.observe(viewLifecycleOwner) { recipeId ->
            val direction = FeedFragmentDirections.actionFeedFragmentToRecipeCardFragment(recipeId)
            findNavController().navigate(direction)
        }


        binding.buttonToFilters.setOnClickListener {
            val direction = FeedFragmentDirections.actionFeedFragmentToFilterFragment()
            findNavController().navigate(direction)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                viewModel.searchDatabase(text)
                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    adapter.submitList(recipes)
                }
                return false
            }
        })

    }.root

}
