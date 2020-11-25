package com.example.resepque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.resepque.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    long insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipe_table WHERE id=:id")
    Recipe findById(long id);

    @Query("SELECT * FROM recipe_table ORDER BY LOWER(title) ASC")
    LiveData<List<Recipe>> findAllOrdered();
}