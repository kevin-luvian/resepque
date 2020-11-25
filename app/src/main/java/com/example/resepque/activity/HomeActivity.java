package com.example.resepque.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;
import com.example.resepque.adapter.RecipeCardViewAdapter;
import com.example.resepque.fragment.AppBarFragment;
import com.example.resepque.model.Recipe;
import com.example.resepque.viewmodel.RecipesViewModel;
import com.example.resepque.viewmodel.factory.RecipesViewModelFactory;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecipesViewModel recipesViewModel;

    EditText search;
    ImageView searchIcon;
    ImageButton buttonCreate;
    RecipeCardViewAdapter recipesAdapter;
    RecyclerView recipesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setFragmentAppBar();

        RecipesViewModelFactory recipesViewModelFactory = new RecipesViewModelFactory(getApplication());
        recipesViewModel = new ViewModelProvider(this, recipesViewModelFactory).get(RecipesViewModel.class);

        search = findViewById(R.id.search_bar);
        searchIcon = findViewById(R.id.search_icon);
        buttonCreate = findViewById(R.id.button_create);
        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        search.setText("");
        search.addTextChangedListener(new EditTextWatcher());
        searchIcon.setOnClickListener(v -> {
            search.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        });
        buttonCreate.setOnClickListener(v -> launchAddRecipeActivity());
        recipesViewModel.getRecipes().observe(this, recipes -> {
            recipesAdapter.setRecipes(recipes);
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void setupRecyclerView() {
        recipesRecyclerView.setLayoutManager(getLayoutManager());
        recipesAdapter = new RecipeCardViewAdapter();
        recipesAdapter.setListener(this::launchViewRecipeActivity);
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(HomeActivity.this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
                super.setSpanSizeLookup(new SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position % 2 == 0) return 0;
                        return 1;
                    }
                });
            }
        };
    }

    private void setFragmentAppBar() {
        AppBarFragment appBarFragment = new AppBarFragment(AppBarFragment.MENU_NO_OPTIONS);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_top_bar, appBarFragment);
        transaction.commit();
    }

    public void launchViewRecipeActivity(Recipe recipe) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_up, R.anim.slide_out);
    }

    public void launchAddRecipeActivity() {
        Intent intent = new Intent(this, AddEditRecipeActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_up, R.anim.slide_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<Recipe> searchedRecipes = recipesViewModel.searchRecipes(s.toString());
            recipesAdapter.setRecipes(searchedRecipes);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}