package com.example.mobileproject.activity.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.admin.adapter.BookAdapter;
import com.example.mobileproject.api.ApiAdmin;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
//import com.example.mobileproject.item.AdminBookItem;
import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Book;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Util;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookManagerActivity extends AppCompatActivity {
    private List<Book> books;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.book_manager_admin);
        recyclerView = findViewById(R.id.recycler_view);
        bookAdapter = new BookAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getBooks();
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getBooks() {
        String token = Util.getInstance().getToken();
        ApiService.apiService.create(ApiAdmin.class).getBooks(token).enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = new ArrayList<>();
                    List<BookResponse> bookResponses = response.body().getResult();
                    for (BookResponse bookResponse : bookResponses) {
                        Book book = bindToBook(bookResponse);
                        books.add(book);
                    }
                    setBooks(books);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
                Log.e("BookManagerActivity", "onFailure: " + throwable.getMessage());
            }
        });
    }

    private Book bindToBook(BookResponse bookResponse) {
        Book book = Book.builder().id(bookResponse.getId())
                .title(bookResponse.getTitle())
                .authors(bookResponse.getAuthors())
                .categories(bookResponse.getCategories())
                .comments(bookResponse.getComments())
                .price(bookResponse.getPrice())
                .pages(bookResponse.getPages())
                .imageUrl(bookResponse.getImageUrl())
                .status(bookResponse.getStatus())
                .content(bookResponse.getContent())
                .build();
        return book;
    }
    private void setBooks(List<Book> books){
        bookAdapter.setBooks(books);
        recyclerView.setAdapter(bookAdapter);
    }


}