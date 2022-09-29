package ru.netology.myrecipe.data


import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    var steps: List<Step?>,
    val favoriteForMe: Boolean = false
)


//@Serializable
//class EditRecipeResult(
//    val newTitle: String?,
//    val newCategory: String?,
//    val newSteps: List<Step>
//)


@Serializable
data class Step(
    val id: Int,
    val text: String?,
    val idRecipe: Int
): java.io.Serializable



