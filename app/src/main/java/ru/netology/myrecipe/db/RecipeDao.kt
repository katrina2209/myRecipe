package ru.netology.myrecipe.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE favoriteForMe = 1 ORDER BY title ASC")
    fun getFavorite(): LiveData<List<RecipeEntity>>

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query(
        """
        UPDATE recipes SET 
        title = :title,
        author = :author,
        category = :category,
        steps = :steps,
        pictureUrl = :pictureUrl
        WHERE id = :id
        """
    )
    fun updateContentById(
        id: Int,
        title: String,
        author: String,
        category: String,
        steps: String,
        pictureUrl: String
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

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :searchQuery || '%'ORDER BY title ASC")
    fun searchDatabase(searchQuery: String): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE favoriteForMe = 1 AND title LIKE '%' || :searchQuery || '%' ORDER BY title ASC")
    fun searchFavoriteDatabase(searchQuery: String): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE category IN (:categories)")
    fun searchByCategory (categories: ArrayList<String>): LiveData<List<RecipeEntity>>
}

