package com.example.resepque.viewmodel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.resepque.viewmodel.RecipesViewModel;

public class RecipesViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;

    public RecipesViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == RecipesViewModel.class) {
            return (T) new RecipesViewModel(application);
        } else {
            return null;
        }
    }
}
