package com.example.resepque.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;
import com.example.resepque.model.Content;
import com.example.resepque.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class ContentViewAdapter extends RecyclerView.Adapter<ContentViewAdapter.ViewHolder> {
    private List<Content> contents;
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final Repository repository;

    public ContentViewAdapter(Application application) {
        repository = new Repository(application);
        contents = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_content, parent, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content content = contents.get(position);
        holder.itemRecyclerView.setLayoutManager(getLayoutManager(holder.itemRecyclerView.getContext()));
        holder.itemRecyclerView.setAdapter(holder.itemViewAdapter);
        holder.itemRecyclerView.setRecycledViewPool(viewPool);
        holder.setContent(content);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
        notifyDataSetChanged();
    }

    private RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemRecyclerView;
        ItemViewAdapter itemViewAdapter;
        TextView contentName;

        public ViewHolder(@NonNull View view) {
            super(view);
            itemRecyclerView = view.findViewById(R.id.content_item_recycler_view);
            itemViewAdapter = new ItemViewAdapter();
            contentName = view.findViewById(R.id.content_name);
        }

        public void setContent(Content content) {
            contentName.setText(String.format("%s :", content.getName()));
            itemViewAdapter.setItems(content.getItems());
        }
    }
}
