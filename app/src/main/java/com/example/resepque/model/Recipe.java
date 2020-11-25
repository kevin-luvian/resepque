package com.example.resepque.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_table")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private Uri imageUri;

    public Recipe() {
        this("New Recipe");
    }

    public Recipe(String title) {
        this.id = 0;
        this.title = title;
        this.imageUri = Uri.parse("");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean titleContains(String text) {
        return title.toLowerCase().contains(text.toLowerCase());
    }

    public String getImageFilename() {
        return String.format("%s_Whatedpak.jpg", id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUri=" + imageUri.toString() +
                '}';
    }
}
