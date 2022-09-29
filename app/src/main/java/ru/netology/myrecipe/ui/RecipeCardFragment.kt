package ru.netology.myrecipe.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import ru.netology.myrecipe.R
import ru.netology.myrecipe.adapter.RecipesAdapter
import ru.netology.myrecipe.databinding.RecipeCardFragmentBinding
import ru.netology.myrecipe.viewModel.RecipeViewModel


class RecipeCardFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val args by navArgs<RecipeCardFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RecipeCardFragmentBinding.inflate(layoutInflater)

        val recipeId = args.recipeId
        val viewHolder = RecipesAdapter.ViewHolder(binding.recipeCardFragmentLayout, viewModel)
        binding.recipeCardFragmentLayout.content.visibility = View.VISIBLE

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            val recipe = recipes.find { it.id == recipeId } ?: return@observe
            viewHolder.bind(recipe)


            binding.recipeCardFragmentLayout.options.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClicked(recipe)
                                findNavController().navigate(R.id.feed_fragment)
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClicked(recipe)

                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }


        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val initialSteps: String? = Gson().toJson(recipe.steps)
            val direction =
                RecipeCardFragmentDirections.actionRecipeCardFragmentToRecipeContentFragment(
                    recipe.title,
                    recipe.category,
                    initialSteps
                )
            findNavController().navigate(direction)
        }
        return binding.root

    }
}

