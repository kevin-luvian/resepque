package com.example.resepque.adapter;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;
import com.example.resepque.model.Recipe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RecipeCardViewAdapter extends RecyclerView.Adapter<RecipeCardViewAdapter.ViewHolder> {
    private RecipeCardViewAdapter.OnClickListener listener;
    private List<Recipe> recipes;

    public RecipeCardViewAdapter() {
        recipes = new ArrayList<>();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        if (recipes != null) this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new RecipeCardViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.title.setText(recipe.getTitle());
        setupImage(holder, recipe.getImageUri());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private void setupImage(ViewHolder holder, Uri imageUri) {
        if (imageUri != null && !imageUri.equals(Uri.EMPTY)) {
            holder.image.setImageURI(imageUri);
            if (holder.image.getDrawable() == null)
                holder.image.setImageResource(R.drawable.empty_gallery);
        } else {
            holder.image.setImageResource(R.drawable.empty_gallery);
        }
    }

    public static boolean checkURIResource(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        boolean doesExist = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close();
        }
        return doesExist;
    }

    public interface OnClickListener {
        void onClick(Recipe recipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.recipe_image);
            title = view.findViewById(R.id.recipe_title);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(recipes.get(position));
                }
            });
        }
    }

    private static class SetImageAsyncTask extends AsyncTask<Uri, Void, Void> {
        private final WeakReference<ViewHolder> weakReference;

        SetImageAsyncTask(ViewHolder viewHolder) {
            weakReference = new WeakReference<ViewHolder>(viewHolder);
        }

        @Override
        protected Void doInBackground(Uri... imageUris) {
            ViewHolder reference = weakReference.get();
            if (reference != null) {
                Uri imageUri = imageUris[0];
                if (imageUri != null && !imageUri.equals(Uri.EMPTY))
                    reference.image.setImageURI(imageUri);
                else
                    reference.image.setImageResource(R.drawable.empty_gallery);
            }
            return null;
        }
    }
}
