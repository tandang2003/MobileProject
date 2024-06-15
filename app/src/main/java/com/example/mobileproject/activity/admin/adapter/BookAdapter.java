package com.example.mobileproject.activity.admin.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.google.android.material.button.MaterialButton;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView title, author;
        private int position;
        private MaterialButton visible, invisible;

        public BookViewHolder(@NonNull View view) {
            super(null);
            title = view.findViewById(R.id.book_title_value);
            author = view.findViewById(R.id.book_author_value);
            visible = view.findViewById(R.id.visbile);
            invisible = view.findViewById(R.id.close);
        }
    }
}
