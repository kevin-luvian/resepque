package com.example.resepque.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "content_table")
public class Content {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ForeignKey(
            entity = Recipe.class,
            parentColumns = "id",
            childColumns = "recipeId",
            onDelete = ForeignKey.CASCADE
    )
    private long recipeId;
    private String name;
    private List<String> items;

    public Content() {
        this(0);
    }

    public Content(long recipeId) {
        this.id = 0;
        this.recipeId = recipeId;
        this.name = "New Recipe";
        this.items = new ArrayList<String>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void createItem(String text) {
        items.add(text);
    }

    public void updateItem(int index, String text) {
        if (-1 < index && index < items.size())
            items.set(index, text);
    }

    public void removeItem(int index) {
        if (-1 < index && index < items.size())
            items.remove(index);
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
