package com.example.mobileproject.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;

public class ReadingActivity extends AppCompatActivity {

    private ImageButton backIcon;
    private TextView booksRead, bookTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_book);

        backIcon = findViewById(R.id.backIcon);
        booksRead = findViewById(R.id.books_read);
        bookTitles = findViewById(R.id.exploreText);

        // Get the book content from the intent
        String bookContent = getIntent().getStringExtra("BOOK_CONTENT");
        String bookTitle = getIntent().getStringExtra("BOOK_TITLE");

        // Set the book content to the TextView
        booksRead.setText(bookContent);
        bookTitles.setText(bookTitle);

        // Set click listener for back button
        backIcon.setOnClickListener(v -> onBackPressed());
    }
}