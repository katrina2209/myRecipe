package ru.netology.myrecipe.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import ru.netology.myrecipe.R
import ru.netology.myrecipe.adapter.RecipesAdapter
import ru.netology.myrecipe.data.Step
import ru.netology.myrecipe.databinding.FeedFragmentBinding

import ru.netology.myrecipe.viewModel.RecipeViewModel


class FeedFragment : Fragment() {

    private val gson = Gson()
    private val viewModel: RecipeViewModel by activityViewModels()
    private lateinit var recyclerViewAdapter: RecipesAdapter


    @RequiresApi(33)
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
            val newRecipeSteps = gson.fromJson(bundle.getString("newRecipeSteps"), Array<Step>::class.java) as List<Step>



            viewModel.onSaveButtonClicked(
                newRecipeTitle,
                newRecipeCategory.orEmpty(),
                newRecipeSteps

            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->


        val adapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter


        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    binding.searchView.visibility = VISIBLE
                    binding.searchView.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener {
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

                    viewModel.data.observe(viewLifecycleOwner) { recipes ->
                        adapter.submitList(recipes)
                    }
                    true
                }
                R.id.action_filters -> {
                    val direction = FeedFragmentDirections.actionFeedFragmentToFilterFragment()
                    findNavController().navigate(direction)
                    true
                }
                R.id.action_clear_filters -> {
                    viewModel.clearFilters()
                    viewModel.data.observe(viewLifecycleOwner) { recipes ->
                        adapter.submitList(recipes)
                    }
                    true
                }
                else -> false
            }
        }


        viewModel.data.observe(viewLifecycleOwner) { recipes ->

            if (recipes.isNullOrEmpty()) {
                binding.emptyDataPic.visibility = VISIBLE
                binding.emptyText.visibility = VISIBLE
                binding.recipesRecyclerView.visibility = GONE


            } else {
                binding.recipesRecyclerView.visibility = VISIBLE
                binding.recipesRecyclerView.scrollToPosition(0)
                binding.emptyDataPic.visibility = GONE
                binding.emptyText.visibility = GONE
                adapter.submitList(recipes)
            }
        }

        setFragmentResultListener(
            requestKey = "chosenCategories"
        ) { requestKey, bundle ->
            if (requestKey != "chosenCategories") return@setFragmentResultListener

            val chosenCategories = bundle.getStringArrayList("listOfChosenCategories")
            if (chosenCategories != null) {
                viewModel.filterByCategory(chosenCategories)
                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    adapter.submitList(recipes)
                }
            }
        }


        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val initialSteps: String? = gson.toJson(recipe.steps)
            val direction =
                FeedFragmentDirections.actionFeedFragmentToRecipeContentFragment(
                    recipe.title,
                    recipe.category,
                    initialSteps
                )
            findNavController().navigate(direction)
        }


        viewModel.navigateToRecipeCardFragmentEvent.observe(viewLifecycleOwner) { recipeId ->
            val direction = FeedFragmentDirections.actionFeedFragmentToRecipeCardFragment(recipeId)
            findNavController().navigate(direction)
        }

    }.root

}
