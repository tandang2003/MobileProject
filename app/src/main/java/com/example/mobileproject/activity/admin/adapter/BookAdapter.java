package com.example.mobileproject.activity.admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.service.quicksettings.Tile;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiAdmin;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.request.ChangeStatusBookRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.model.Book;
import com.example.mobileproject.util.Util;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_info_admin, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Log.i("BookAdapter", "onBindViewHolder position: " + position);
        Book book = books.get(position);
        if (book == null)
            return;
        StringBuilder author = new StringBuilder();
        for (int i = 0; i < book.getAuthors().size(); i++) {
            author.append(book.getAuthors().get(i).getName());
            if (i != book.getAuthors().size() - 1) {
                author.append(", ");
            }
        }
        Picasso.get().load(book.getImageUrl()).into(holder.imageView);
        holder.author.setText(author.toString());
        holder.position = position;
        holder.title.setText(book.getTitle());
        holder.title.setSelected(true);
        holder.switchButton.setChecked(book.getStatus() == 1);
        holder.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setSwitchButton(books.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        if (books == null)
            return 0;
        return books.size();
    }

    protected class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView title, author;
        private int position;
        private ImageView imageView;
        private Switch switchButton;

        public BookViewHolder(@NonNull View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.book_title_value);
            author = view.findViewById(R.id.book_author_value);
            imageView = view.findViewById(R.id.book_image);
            title.setSelected(true);
            author.setSelected(true);
            switchButton = view.findViewById(R.id.switchButton);
        }

    }
    private void setSwitchButton(Long bookId) {
        String token = Util.getInstance().getToken();
        ApiService.apiService.create(ApiAdmin.class).changeBookStatus(token, ChangeStatusBookRequest.builder()
                .bookId(bookId).build()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    Log.i("BookAdapter", "onResponse: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                Log.e("BookAdapter", "onFailure: " + throwable.getMessage());
            }
        });
    }

}
