package com.example.resepque.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.resepque.dao.ContentDAO;
import com.example.resepque.dao.RecipeDAO;
import com.example.resepque.database.Database;
import com.example.resepque.model.Content;
import com.example.resepque.model.Recipe;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Repository {
    private ExecutorService daoExecutor;
    private final RecipeDAO recipeDAO;
    private final ContentDAO contentDAO;

    public Repository(Application application) {
        Database database = Database.getInstance(application);
        daoExecutor = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        recipeDAO = database.recipeDAO();
        contentDAO = database.contentDAO();
    }

    public Recipe getRecipeById(long id) {
        try {
            GetRecipeByIdAsyncTask task = new GetRecipeByIdAsyncTask(this);
            return task.execute(id).get();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insertRecipe(Recipe recipe) {
        try {
            InsertRecipeAsyncTask task = new InsertRecipeAsyncTask(this);
            return task.execute(recipe).get();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateRecipe(Recipe recipe) {
        daoExecutor.execute(() -> recipeDAO.update(recipe));
    }

    public void deleteRecipe(Recipe recipe) {
        daoExecutor.execute(() -> recipeDAO.delete(recipe));
    }

    public LiveData<List<Recipe>> getLiveRecipes() {
        try {
            LiveRecipeAsyncTask task = new LiveRecipeAsyncTask(this);
            return task.execute().get();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insertContent(Content content) {
        try {
            InsertContentAsyncTask task = new InsertContentAsyncTask(this);
            return task.execute(content).get();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateContent(Content content) {
        daoExecutor.execute(() -> {
            contentDAO.update(content);
        });
    }

    public void deleteContent(Content content) {
        daoExecutor.execute(() -> {
            contentDAO.delete(content);
        });
    }

    public List<Content> getContentsByRecipeId(long recipeId) {
        try {
            FindContentsByRecipeId task = new FindContentsByRecipeId(this);
            return task.execute(recipeId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetRecipeByIdAsyncTask extends AsyncTask<Long, Void, Recipe> {
        private final WeakReference<Repository> weakReference;

        GetRecipeByIdAsyncTask(Repository repository) {
            weakReference = new WeakReference<Repository>(repository);
        }

        @Override
        protected Recipe doInBackground(Long... ids) {
            Repository reference = weakReference.get();
            if (reference == null) return null;
            return reference.recipeDAO.findById(ids[0]);
        }
    }

    private static class InsertRecipeAsyncTask extends AsyncTask<Recipe, Void, Long> {
        private final WeakReference<Repository> weakReference;

        InsertRecipeAsyncTask(Repository repository) {
            weakReference = new WeakReference<Repository>(repository);
        }

        @Override
        protected Long doInBackground(Recipe... recipes) {
            Repository reference = weakReference.get();
            if (reference == null) return null;
            return reference.recipeDAO.insert(recipes[0]);
        }
    }

    private static class InsertContentAsyncTask extends AsyncTask<Content, Void, Long> {
        private final WeakReference<Repository> weakReference;

        InsertContentAsyncTask(Repository repository) {
            weakReference = new WeakReference<Repository>(repository);
        }

        @Override
        protected Long doInBackground(Content... contents) {
            Repository reference = weakReference.get();
            if (reference == null) return null;
            return reference.contentDAO.insert(contents[0]);
        }
    }

    private static class FindContentsByRecipeId extends AsyncTask<Long, Void, List<Content>> {
        private final WeakReference<Repository> weakReference;

        FindContentsByRecipeId(Repository repository) {
            weakReference = new WeakReference<Repository>(repository);
        }

        @Override
        protected List<Content> doInBackground(Long... recipeIds) {
            Repository reference = weakReference.get();
            if (reference == null | recipeIds[0] == 0) return null;
            return reference.contentDAO.findAllByRecipeId(recipeIds[0]);
        }
    }

    private static class LiveRecipeAsyncTask extends AsyncTask<Void, Void, LiveData<List<Recipe>>> {
        private final WeakReference<Repository> weakReference;

        LiveRecipeAsyncTask(Repository repository) {
            weakReference = new WeakReference<Repository>(repository);
        }

        @Override
        protected LiveData<List<Recipe>> doInBackground(Void... voids) {
            Repository reference = weakReference.get();
            if (reference == null) return null;
            return reference.recipeDAO.findAllOrdered();
        }
    }
}
