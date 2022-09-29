package ru.netology.myrecipe.db

import androidx.room.*
import ru.netology.myrecipe.data.Step

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "category")
    val category: String = "",
//    @ColumnInfo(name = "steps")
//    val steps: String = "",
       @ColumnInfo(name = "favoriteForMe")
    val favoriteForMe: Boolean = false
    )

class RecipeWithSteps(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idRecipe"
    )
    val steps: List<StepEntity>
)