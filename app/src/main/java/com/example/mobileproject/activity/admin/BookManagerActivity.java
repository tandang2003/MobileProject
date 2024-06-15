package com.example.mobileproject.activity.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
//import com.example.mobileproject.item.AdminBookItem;
import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Book;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookManagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.book_manager_admin);
//        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
//                if (response.isSuccessful()) {
//                    List<BookResponse> bookResponses = response.body().getResult();
//                    for (BookResponse bookResponse : bookResponses) {
//                        View view = LayoutInflater.from(BookManagerActivity.this).inflate(R.layout.book_info_admin, null);
//                        AdminBookItem adminBookItem = new AdminBookItem(view);
//                        StringBuilder author = new StringBuilder();
//                        for (int i = 0; i < bookResponse.getAuthors().size(); i++) {
//                            author.append(bookResponse.getAuthors().get(i).getName());
//                            if (i != bookResponse.getAuthors().size() - 1) {
//                                author.append(", ");
//                            }
//                            adminBookItem.bind((int) bookResponse.getId(), bookResponse.getTitle(), author.toString());
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
//
//            }
//        });


    }
}