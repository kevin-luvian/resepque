package com.example.resepque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.resepque.model.Content;

import java.util.List;

@Dao
public interface ContentDAO {
    @Insert
    long insert(Content content);

    @Update
    void update(Content content);

    @Delete
    void delete(Content content);

    @Query("SELECT * FROM content_table WHERE recipeId=:recipeId")
    List<Content> findAllByRecipeId(long recipeId);
}