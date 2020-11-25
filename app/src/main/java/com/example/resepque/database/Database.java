package com.example.resepque.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.resepque.dao.ContentDAO;
import com.example.resepque.dao.RecipeDAO;
import com.example.resepque.model.Content;
import com.example.resepque.model.Recipe;

@androidx.room.Database(entities = {Recipe.class, Content.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract RecipeDAO recipeDAO();

    public abstract ContentDAO contentDAO();

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitiateDbAsyncTask(instance).execute();
        }
    };

    private static class InitiateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final RecipeDAO recipeDAO;

        private InitiateDbAsyncTask(Database database) {
            this.recipeDAO = database.recipeDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recipeDAO.insert(new Recipe("resep satu"));
            recipeDAO.insert(new Recipe("resep dua"));
            return null;
        }
    }
}