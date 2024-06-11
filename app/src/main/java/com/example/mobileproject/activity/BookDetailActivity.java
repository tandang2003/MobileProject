package com.example.mobileproject.activity;

import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

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

        // Get data from intent
        String title = getIntent().getStringExtra("BOOK_TITLE");
        String author = getIntent().getStringExtra("BOOK_AUTHOR");
        String description = getIntent().getStringExtra("BOOK_DESCRIPTION");
        String imageUrl = getIntent().getStringExtra("BOOK_IMAGE_URL");

        // Set data to views
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookDescription.setText(description);
        Picasso.get().load(imageUrl).into(bookCover);

        // Set click listeners
        backButton.setOnClickListener(v -> onBackPressed());

        moreOptionsButton.setOnClickListener(v -> {
            // Handle more options click
        });

        downloadButton.setOnClickListener(v -> {
            // Handle download button click
        });

        commentButton.setOnClickListener(v -> {
            // Handle comment button click
        });
    }

    private List<Book> getSampleBooks() {
        return new ArrayList<>();
    }
}
