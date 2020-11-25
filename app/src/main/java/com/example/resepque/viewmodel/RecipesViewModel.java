package com.example.resepque.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.resepque.model.Recipe;
import com.example.resepque.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipesViewModel extends ViewModel {
    private final Repository repository;
    private LiveData<List<Recipe>> recipes;

    public RecipesViewModel(Application application) {
        this.repository = new Repository(application);
        recipes = repository.getLiveRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public List<Recipe> searchRecipes(String title) {
        List<Recipe> searchedRecipes = new ArrayList<>();
        for (Recipe recipe : Objects.requireNonNull(recipes.getValue())) {
            if (recipe.titleContains(title)) searchedRecipes.add(recipe);
        }
        return searchedRecipes;
    }
}
