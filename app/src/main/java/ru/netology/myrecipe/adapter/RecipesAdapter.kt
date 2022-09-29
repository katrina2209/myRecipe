package ru.netology.myrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipe.R
import ru.netology.myrecipe.data.Recipe
import ru.netology.myrecipe.databinding.RecipeBinding

class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecipeBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.favorite.setOnClickListener {
                listener.onFavoriteClicked(recipe)
            }

            binding.options.setOnClickListener {
                popupMenu.show()
            }

            binding.root.setOnClickListener {
                listener.onRecipeClicked(recipe)
            }


        }

        fun bind(recipe: Recipe) {

            this.recipe = recipe
            with(binding) {

                title.text = recipe.title
                author.text = recipe.author
                category.text = recipe.category
                content.text = recipe.steps?.joinToString("\n") ?: ""
                favorite.isChecked = recipe.favoriteForMe
            }
        }
    }


    private object DiffCallBack : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}