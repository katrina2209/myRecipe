package ru.netology.myrecipe.db

import ru.netology.myrecipe.data.Recipe
import ru.netology.myrecipe.data.Step


//internal fun RecipeEntity.toModel() = Recipe(
//    id = id,
//    title = title,
//    author = author,
//    category = category,
//    steps = steps,//converter.toList(steps),
//    favoriteForMe = favoriteForMe,
//
//    )

internal fun Recipe.toEntity() =
    RecipeEntity(
        id = id,
        title = title,
        author = author,
        category = category,
        favoriteForMe = favoriteForMe
    )

internal fun RecipeWithSteps.toRecipe() = Recipe(
    id = recipe.id,
    author = recipe.author,
    title = recipe.title,
    category = recipe.category,
    steps = steps.map { it.toStep() },
    favoriteForMe = recipe.favoriteForMe,

    )

internal fun StepEntity.toStep() = Step(
    id = id,
    text = text,
    idRecipe = idRecipe
)

internal fun Step.toEntity() = text?.let {
    StepEntity(
        id = id,
        text = it,
        idRecipe = idRecipe
    )
}
