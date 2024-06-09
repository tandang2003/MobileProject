package com.example.mobileproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Book;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private ImageButton backButton, moreOptionsButton;
    private ImageView bookCover;
    private TextView bookTitle, bookAuthor, bookDescription, ratingTitle;
    private MaterialButton downloadButton, commentButton;
    private RecyclerView reviewRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        // Initialize views
        backButton = findViewById(R.id.imageButton);
        moreOptionsButton = findViewById(R.id.imageButton2);
        bookCover = findViewById(R.id.bookCover);
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookDescription = findViewById(R.id.bookDescription);
        ratingTitle = findViewById(R.id.ratingTitle);
        downloadButton = findViewById(R.id.dowloadButton);
        commentButton = findViewById(R.id.commentButton);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        // Set up RecyclerView
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Book> bookList = getSampleBooks(); // Replace with real data
        BookDetailAdapter adapter = new BookDetailAdapter(bookList);
        reviewRecyclerView.setAdapter(adapter);

        // Set click listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle more options click
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle download button click
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle comment button click
            }
        });

         bookTitle.setText("Eragon 3 (Brisingr) – Hòa Kiếm");
         bookAuthor.setText("Tác giả: Christopher Paolini");
         bookDescription.setText("About this book...");
    }

    private List<Book> getSampleBooks() {
        List<Book> books = new ArrayList<>();
        return books;
    }
}
