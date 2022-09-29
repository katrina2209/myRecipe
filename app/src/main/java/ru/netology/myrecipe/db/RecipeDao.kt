package ru.netology.myrecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAll(): LiveData<List<RecipeWithSteps>>

    @Query("SELECT * FROM recipes WHERE favoriteForMe = 1 ORDER BY title ASC")
    fun getFavorite(): LiveData<List<RecipeWithSteps>>

    @Insert
    fun insert(recipe: RecipeEntity)

    @Insert
    fun insertStep(step: StepEntity)

    @Query(
        """
        UPDATE recipes SET 
        title = :title,
        author = :author,
        category = :category
        WHERE id = :id
        """
    )
    fun updateContentById(
        id: Int,
        title: String,
        author: String,
        category: String
    )

    @Query(
        """ 
                UPDATE steps SET
                text = :text,
                idRecipe = :idRecipe
                WHERE id = :id
                """
    )
    fun updateStep(
        id: Int,
        text: String,
        idRecipe: Int
    )


    @Query(
        """
        UPDATE recipes SET
        favoriteForMe = CASE WHEN favoriteForMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun favoriteById(id: Int)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Int)

    @Query("DELETE FROM steps WHERE id = :id")
    fun deleteStep(id: Int)

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :searchQuery || '%'ORDER BY title ASC")
    fun searchDatabase(searchQuery: String): LiveData<List<RecipeWithSteps>>

    @Query("SELECT * FROM recipes WHERE favoriteForMe = 1 AND title LIKE '%' || :searchQuery || '%' ORDER BY title ASC")
    fun searchFavoriteDatabase(searchQuery: String): LiveData<List<RecipeWithSteps>>

    @Query("SELECT * FROM recipes WHERE category IN (:categories) ORDER BY title ASC")
    fun searchByCategory(categories: ArrayList<String>): LiveData<List<RecipeWithSteps>>
}

