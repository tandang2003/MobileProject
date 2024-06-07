package com.example.mobileproject.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Book;
import java.util.List;

public class BookDetailAdapter extends RecyclerView.Adapter<BookDetailAdapter.BookViewHolder> {

    private final List<Book> books;

    public BookDetailAdapter(List<Book> books) {
        this.books = books;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookTitle;
        TextView bookCategory;
        TextView bookAuthor;
        ImageView moreOptions;

        BookViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookCategory = itemView.findViewById(R.id.book_category);
            bookAuthor = itemView.findViewById(R.id.book_author);
//            moreOptions = itemView.findViewById(R.id.more_options);
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_details, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookImage.setImageResource(Integer.parseInt(book.getImageUrl()));
        holder.bookTitle.setText(book.getTitle());
        holder.bookCategory.setText(book.getCategories().toString()); // Assuming you have a method to get categories as a String
        holder.bookAuthor.setText(book.getAuthors().toString()); // Assuming you have a method to get authors as a String
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
