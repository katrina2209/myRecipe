package ru.netology.myrecipe.data

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val steps: String,
    val pictureUrl: String? = null,
    val favoriteForMe: Boolean = false
)


@Serializable
class EditRecipeResult(
    val newTitle: String?,
    val newCategory: String?,
    val newSteps: String,
    val pictureUrl: String? = null
)



