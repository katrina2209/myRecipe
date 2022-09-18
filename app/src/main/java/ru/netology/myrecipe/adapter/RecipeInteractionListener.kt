package ru.netology.myrecipe.adapter

import ru.netology.myrecipe.data.Recipe

interface RecipeInteractionListener {
    fun onFavoriteClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onRecipeClicked (recipe: Recipe)
}