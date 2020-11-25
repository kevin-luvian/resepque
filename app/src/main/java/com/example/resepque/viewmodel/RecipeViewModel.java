package com.example.resepque.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.resepque.model.Content;
import com.example.resepque.model.Recipe;
import com.example.resepque.repository.Repository;

import java.util.List;

public class RecipeViewModel extends ViewModel {
    private final Repository repository;
    private Recipe recipe;
    private MutableLiveData<List<Content>> contents;

    public RecipeViewModel(@NonNull Application application) {
        repository = new Repository(application);
        contents = new MutableLiveData<List<Content>>();
    }

    public void updateRecipe(Recipe recipe) {
        repository.updateRecipe(recipe);
    }

    public void removeRecipe() {
        repository.deleteRecipe(recipe);
        recipe = null;
        contents = null;
    }

    public void setRecipeById(long id) {
        setRecipe(repository.getRecipeById(id));
    }

    public void setRecipe(Recipe recipe) {
        if (recipe == null) {
            recipe = new Recipe();
            recipe.setId(repository.insertRecipe(recipe));
        }
        this.recipe = recipe;
        refreshContents();
    }

    public void updateRecipeImageUri(Uri imageUri) {
        recipe.setImageUri(imageUri);
        updateRecipe(recipe);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public MutableLiveData<List<Content>> getContents() {
        return contents;
    }

    public void refreshContents() {
        contents.setValue(repository.getContentsByRecipeId(recipe.getId()));
    }

    public void createContent() {
//        Content content = repository.insertContent(new Content(recipe.getId()));
//        contents.getValue().add(content);
        repository.insertContent(new Content(recipe.getId()));
        refreshContents();
    }

    public void updateContent(Content content) {
        repository.updateContent(content);
    }

    public void removeContent(Content content) {
        repository.deleteContent(content);
        refreshContents();
//        contents.getValue().remove(content);
    }
}