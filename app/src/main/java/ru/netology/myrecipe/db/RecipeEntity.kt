package ru.netology.myrecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "steps")
    val steps: String = "",
    @ColumnInfo(name = "pictureUrl")
    val pictureUrl: String? = null,
    @ColumnInfo(name = "favoriteForMe")
    val favoriteForMe: Boolean = false
    )

