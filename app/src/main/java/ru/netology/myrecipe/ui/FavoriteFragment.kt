package ru.netology.myrecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import ru.netology.myrecipe.adapter.RecipesAdapter
import ru.netology.myrecipe.data.Step
import ru.netology.myrecipe.databinding.FavoriteFragmentBinding

import ru.netology.myrecipe.viewModel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by activityViewModels()


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
            //val newRecipeSteps = bundle.getString("newRecipeSteps") //?.split("&")
            val newPictureUrl = bundle.getString("newPictureUrl")

            viewModel.onSaveButtonClicked(
                newRecipeTitle,
                newRecipeCategory.orEmpty(),
                emptyList(),//newRecipeSteps.orEmpty(), //.toMutableList(),
                newPictureUrl
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoriteFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter

        binding.searchView.clearFocus()
        binding.recipesRecyclerView.requestFocus()

        viewModel.dataFavorite.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNullOrEmpty()) {
                binding.emptyDataPic.visibility = View.VISIBLE
                binding.emptyText.visibility = View.VISIBLE
                binding.recipesRecyclerView.visibility = View.GONE
                binding.searchView.visibility = View.GONE
            } else {
                binding.recipesRecyclerView.visibility = View.VISIBLE
                binding.emptyDataPic.visibility = View.GONE
                binding.emptyText.visibility = View.GONE
                binding.searchView.visibility = View.VISIBLE
                adapter.submitList(recipes)
            }
        }


        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val direction =
                FavoriteFragmentDirections.actionFavoriteFragmentToRecipeContentFragment(
                    recipe.title,
                    recipe.category,
                    Gson().toJson(recipe.steps)
                )
            findNavController().navigate(direction)
        }


        viewModel.navigateToRecipeCardFragmentEvent.observe(owner = viewLifecycleOwner) { recipeId ->
            val direction =
                FavoriteFragmentDirections.actionFavoriteFragmentToRecipeCardFragment(recipeId)
            findNavController().navigate(direction)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                viewModel.searchFavoriteDataBase(text)
                viewModel.dataFavorite.observe(viewLifecycleOwner) { recipes ->
                    adapter.submitList(recipes)
                }
                return false
            }
        })
    }.root
}