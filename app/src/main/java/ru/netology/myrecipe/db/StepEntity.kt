package ru.netology.myrecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


    @Entity(
        tableName = "steps",
        foreignKeys = [ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["idRecipe"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
    )
    class StepEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int,
        @ColumnInfo(name = "idRecipe")
        val idRecipe: Int,
        @ColumnInfo(name = "text")
        val text: String
    )
