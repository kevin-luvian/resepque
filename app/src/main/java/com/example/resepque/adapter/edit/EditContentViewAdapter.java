package com.example.resepque.adapter.edit;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;
import com.example.resepque.model.Content;
import com.example.resepque.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class EditContentViewAdapter extends RecyclerView.Adapter<EditContentViewAdapter.ViewHolder> {
    private ContentListener listener;
    private List<Content> contents;
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final Repository repository;

    public EditContentViewAdapter(Application application) {
        repository = new Repository(application);
        contents = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_content_edit, parent, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content content = contents.get(position);
        holder.itemRecyclerView.setLayoutManager(getLayoutManager(holder.itemRecyclerView.getContext()));
        holder.itemRecyclerView.setAdapter(holder.editItemViewAdapter);
        holder.itemRecyclerView.setRecycledViewPool(viewPool);
        holder.setContent(content);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    private RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
        notifyDataSetChanged();
    }

    public void setListener(ContentListener listener) {
        this.listener = listener;
    }

    public interface ContentListener {
        void refreshContents();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Content content = null;
        RecyclerView itemRecyclerView;
        EditItemViewAdapter editItemViewAdapter;
        EditText contentName;
        ImageButton buttonAdd;
        ImageButton buttonDelete;

        public ViewHolder(@NonNull View view) {
            super(view);
            itemRecyclerView = view.findViewById(R.id.content_item_recycler_view);
            editItemViewAdapter = new EditItemViewAdapter();
            contentName = view.findViewById(R.id.content_name);
            buttonAdd = view.findViewById(R.id.button_add);
            buttonDelete = view.findViewById(R.id.button_delete);

            editItemViewAdapter.setListener(new EditItemViewAdapter.ItemListener() {
                @Override
                public void updateItem(int index, String text) {
                    content.updateItem(index, text);
                    repository.updateContent(content);
                }

                @Override
                public void removeItem(int index) {
                    content.removeItem(index);
                    repository.updateContent(content);
                    refreshContentAdapter();
                }
            });

            buttonAdd.setOnClickListener(v -> {
                content.createItem("");
                repository.updateContent(content);
                refreshContentAdapter();
            });

            buttonDelete.setOnClickListener(v -> {
                repository.deleteContent(content);
                listener.refreshContents();
            });
        }

        public void setContent(Content content) {
            this.content = content;
            refreshContentAdapter();
            contentName.addTextChangedListener(new EditTextWatcher());
        }

        public void refreshContentAdapter() {
            contentName.setText(content.getName());
            editItemViewAdapter.setItems(content.getItems());
        }

        private class EditTextWatcher implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!content.getName().equals(s.toString())) {
                    content.setName(s.toString());
                    repository.updateContent(content);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        }
    }
}
