package com.example.resepque.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;
import com.example.resepque.adapter.ContentViewAdapter;
import com.example.resepque.adapter.RecipeCardViewAdapter;
import com.example.resepque.adapter.edit.EditContentViewAdapter;
import com.example.resepque.fragment.AppBarFragment;
import com.example.resepque.model.Recipe;
import com.example.resepque.viewmodel.RecipeViewModel;
import com.example.resepque.viewmodel.factory.RecipeViewModelFactory;

public class ViewRecipeActivity extends AppCompatActivity {
    public static final String INTENT_OPTION = "result_option";
    public static final String INTENT_RECIPE_ID = "recipe_id";
    public static final int OPTION_BACK = 1;
    public static final int OPTION_UPDATE = 2;
    public static final int EDIT_ACTIVITY_CODE = 1;
    private long recipeId = 0;
    private RecipeViewModel recipeViewModel;
    private RecyclerView contentRecyclerView;
    private ContentViewAdapter contentViewAdapter;
    private ImageView recipeImage;
    private TextView recipeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(getApplication());
        recipeViewModel = new ViewModelProvider(this, recipeViewModelFactory).get(RecipeViewModel.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            recipeId = bundle.getLong(INTENT_RECIPE_ID);
        }

        contentRecyclerView = findViewById(R.id.recipe_content_recycler_view);
        recipeImage = findViewById(R.id.recipe_image);
        recipeText = findViewById(R.id.recipe_title);
        setFragmentAppBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        contentRecyclerView.setLayoutManager(getLayoutManager());
        contentViewAdapter = new ContentViewAdapter(getApplication());
        contentRecyclerView.setAdapter(contentViewAdapter);
        refreshValues();
    }

    public void refreshValues() {
        recipeViewModel.setRecipeById(recipeId);
        Recipe recipe = recipeViewModel.getRecipe();
        recipeText.setText(String.format("%s :", recipe.getTitle()));
        setupImage(recipe.getImageUri());
        contentViewAdapter.setContents(recipeViewModel.getContents().getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ACTIVITY_CODE && resultCode == RESULT_OK && data != null) {
            switch (data.getIntExtra(INTENT_OPTION, 0)) {
                case OPTION_BACK:
                    finish();
                    break;
                case OPTION_UPDATE:
                    recipeId = data.getLongExtra(INTENT_RECIPE_ID, recipeId);
                    refreshValues();
                    break;
                default:
                    break;
            }
        }
    }


    private void setupImage(Uri imageUri) {
        if (imageUri != null && !imageUri.equals(Uri.EMPTY)) {
            recipeImage.setImageURI(imageUri);
            if (recipeImage.getDrawable() == null)
                recipeImage.setImageResource(R.drawable.empty_gallery);
        } else {
            recipeImage.setImageResource(R.drawable.empty_gallery);
        }
    }

    private void setFragmentAppBar() {
        AppBarFragment appBarFragment = new AppBarFragment(AppBarFragment.MENU_BACK_OPTION_EDIT);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_top_bar, appBarFragment);
        transaction.commit();
    }

    public void clickEditOption() {
        launchEditRecipeActivity();
    }

    private void launchEditRecipeActivity() {
        Intent intent = new Intent(this, AddEditRecipeActivity.class);
        intent.putExtra(INTENT_RECIPE_ID, recipeId);
        startActivityForResult(intent, EDIT_ACTIVITY_CODE);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }
}