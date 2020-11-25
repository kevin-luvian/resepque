package com.example.resepque.adapter.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resepque.R;

import java.util.List;

public class EditItemViewAdapter extends RecyclerView.Adapter<EditItemViewAdapter.ViewHolder> {
    private ItemListener listener;
    private List<String> items;

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<String> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_edit, parent, false);
        return new EditItemViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.position = position;
        holder.itemText.setText(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    public interface ItemListener {
        void updateItem(int index, String text);

        void removeItem(int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int position;
        EditText itemText;
        ImageButton buttonDelete;

        public ViewHolder(@NonNull View view) {
            super(view);
            itemText = view.findViewById(R.id.content_item_text);
            buttonDelete = view.findViewById(R.id.button_delete);
            itemText.addTextChangedListener(new EditTextWatcher());
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.removeItem(position);
                }
            });
        }

        private class EditTextWatcher implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.updateItem(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        }
    }
}
