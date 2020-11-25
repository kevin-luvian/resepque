package com.example.resepque.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;
import com.example.resepque.adapter.edit.EditContentViewAdapter;
import com.example.resepque.fragment.AppBarFragment;
import com.example.resepque.model.Recipe;
import com.example.resepque.viewmodel.RecipeViewModel;
import com.example.resepque.viewmodel.factory.RecipeViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditRecipeActivity extends AppCompatActivity {
    public static final int INTENT_IMAGE_CAPTURE_CODE = 7;
    public static final int INTENT_IMAGE_GALLERY_CODE = 11;
    public static final String INTENT_IMAGE_URI = "image_uri";
    public static final String INTENT_RECIPE_ID = "recipe_id";
    private RecipeViewModel recipeViewModel;
    private RecyclerView contentRecyclerView;
    private String imagePath;
    private File imageFile = null;
    private Uri imageUri;

    private EditText editTitle;
    private ImageView recipeImage;
    private Button btnChooseImage;
    private ImageButton btnAddContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(getApplication());
        recipeViewModel = new ViewModelProvider(this, recipeViewModelFactory).get(RecipeViewModel.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            recipeViewModel.setRecipeById(bundle.getLong(INTENT_RECIPE_ID));
        } else {
            recipeViewModel.setRecipe(null);
        }
        contentRecyclerView = findViewById(R.id.recipe_content_recycler_view);
        editTitle = findViewById(R.id.recipe_title);
        recipeImage = findViewById(R.id.recipe_image);
        btnChooseImage = findViewById(R.id.button_choose_image);
        btnAddContent = findViewById(R.id.button_add_content);
        setFragmentAppBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Recipe recipe = recipeViewModel.getRecipe();
        setupImage(recipe.getImageUri());
        editTitle.setText(recipe.getTitle());
        editTitle.addTextChangedListener(new EditTextWatcher());
        btnChooseImage.setOnClickListener(v -> {
            showImagePickerOptions();
        });
        btnAddContent.setOnClickListener(v -> recipeViewModel.createContent());
        contentRecyclerView.setLayoutManager(getLayoutManager());
        EditContentViewAdapter editContentViewAdapter = new EditContentViewAdapter(getApplication());
        editContentViewAdapter.setListener(() -> recipeViewModel.refreshContents());
        contentRecyclerView.setAdapter(editContentViewAdapter);
        recipeViewModel.getContents().observe(this, editContentViewAdapter::setContents);
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File imageFile = createImageFile();
            imageUri = FileProvider.getUriForFile(this,
                    "com.example.resepque.fileprovider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, INTENT_IMAGE_CAPTURE_CODE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            e.printStackTrace();
        }
    }

//    private void launchCameraIntent() {
//        Toast.makeText(this, "Trying to capture image", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            imageFile = createImageFile();
////                Toast.makeText(this, "Image Path: " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//            Log.i("ImageFile", imageFile.getAbsolutePath());
//            System.out.println("ImageFilePath : " + imageFile.getAbsolutePath());
//
//            // Continue only if the File was successfully created
//            if (imageFile != null) {
//                imageUri = FileProvider.getUriForFile(this,
//                        "com.example.resepque.fileprovider",
//                        imageFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent, INTENT_IMAGE_CAPTURE_CODE);
//            }
//        } catch (Exception e) {
//            // Error occurred while creating the File
//            e.printStackTrace();
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, INTENT_IMAGE_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Activity Result Called");
        if (resultCode == Activity.RESULT_OK) {
            Uri imageURI;
            if (data == null)
                Toast.makeText(this, "no image selected", Toast.LENGTH_SHORT).show();
            switch (requestCode) {
                case INTENT_IMAGE_CAPTURE_CODE:
                    System.out.println("Image captured");
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    recipeImage.setImageBitmap(bitmap);
//                    setupImage(imageUri);
                    System.out.println("ImageUri : " + imageUri);
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                    recipeImage.setImageBitmap(myBitmap);
//                    imageURI = Uri.fromFile(imageFile);
//                    setupImage(imageURI);
//                    recipeViewModel.updateRecipeImageUri(imageURI);
                    break;
                case INTENT_IMAGE_GALLERY_CODE:
                    imageURI = data.getData();
                    setupImage(imageURI);
                    recipeViewModel.updateRecipeImageUri(imageURI);
                    break;
                default:
                    break;
            }
        }
    }

    private File createImageFile() {
        // Create an image file name
        File filePath = new File(getFilesDir(), "images");
        File newFile = new File(filePath, recipeViewModel.getRecipe().getImageFilename());
        // Save a file: path for use with ACTION_VIEW intents
        imagePath = newFile.getAbsolutePath();
        return newFile;
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

    private RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    private void setFragmentAppBar() {
        AppBarFragment appBarFragment = new AppBarFragment(AppBarFragment.MENU_BACK_OPTION_DELETE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_top_bar, appBarFragment);
        transaction.commit();
    }

    public void popBack() {
        Intent intent = new Intent();
        intent.putExtra(ViewRecipeActivity.INTENT_OPTION, ViewRecipeActivity.OPTION_UPDATE);
        intent.putExtra(ViewRecipeActivity.INTENT_RECIPE_ID, recipeViewModel.getRecipe().getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void removeThis() {
        recipeViewModel.removeRecipe();
        Intent intent = new Intent();
        intent.putExtra(ViewRecipeActivity.INTENT_OPTION, ViewRecipeActivity.OPTION_BACK);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Recipe recipe = recipeViewModel.getRecipe();
            recipe.setTitle(s.toString());
            recipeViewModel.updateRecipe(recipe);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}