package com.example.resepque.viewmodel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.resepque.viewmodel.RecipeViewModel;

public class RecipeViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;

    public RecipeViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == RecipeViewModel.class) {
            return (T) new RecipeViewModel(application);
        } else {
            return null;
        }
    }
}
