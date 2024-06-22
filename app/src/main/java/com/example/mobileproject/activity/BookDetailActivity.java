package com.example.mobileproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dialog.comment.CommentDialog;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.dto.response.CommentResponse;
import com.example.mobileproject.model.Comment;
import com.example.mobileproject.sharedPreference.GetData;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView bookCover, moreOptionsButton;
    private TextView bookTitle, bookAuthor, bookContent, ratingTitle;
    private MaterialButton readButton, commentButton;
    private LinearLayout cmtListSection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        GetData.init(this);

        backButton = findViewById(R.id.imageButton);
        moreOptionsButton = findViewById(R.id.imageButton2);
        bookCover = findViewById(R.id.bookCover);
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookContent = findViewById(R.id.bookContent);
        ratingTitle = findViewById(R.id.ratingTitle);
        readButton = findViewById(R.id.readButton);
        commentButton = findViewById(R.id.commentButton);
        cmtListSection = findViewById(R.id.cmt_list_section);

        String title = getIntent().getStringExtra("BOOK_TITLE");
        String author = getIntent().getStringExtra("BOOK_AUTHOR");
        String content = getIntent().getStringExtra("BOOK_CONTENT");
        String imageUrl = getIntent().getStringExtra("BOOK_IMAGE_URL");
        String bookId = getIntent().getStringExtra("BOOK_ID");
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookContent.setText(content);
        Picasso.get().load(imageUrl).into(bookCover);

        // Set click listeners
        backButton.setOnClickListener(v -> onBackPressed());

        commentButton.setOnClickListener(v -> showCommentDialog(bookId));
        readButton.setOnClickListener(v -> {
            Intent intent = new Intent(BookDetailActivity.this, ReadingActivity.class);
            intent.putExtra("BOOK_CONTENT", bookContent.getText().toString());
            intent.putExtra("BOOK_TITLE", bookTitle.getText().toString());
            startActivity(intent);
        });

        fetchComments(bookId);
    }

    private void showCommentDialog(String bookId) {
        CommentDialog commentDialog = new CommentDialog();
        Bundle args = new Bundle();
        args.putString("BOOK_ID", bookId);
        commentDialog.setArguments(args);
        commentDialog.setCommentSubmitListener(() -> fetchComments(bookId));
        commentDialog.show(getSupportFragmentManager(), "CommentDialog");
    }


    private void fetchComments(String bookId) {
        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful()) {
                    List<BookResponse> books = response.body().getResult();
                    for (BookResponse book : books) {
                        try {
                            long id = Long.parseLong(bookId);
                            if (book.getId() == id) {
                                displayComments(book.getComments());
                                break;
                            }
                        } catch (NumberFormatException e) {
                            Log.e("BookDetailActivity", "Invalid bookId format: " + bookId);
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable t) {
                Log.e("BookDetailActivity", "Failed to get books comment", t);
            }
        });
    }

    private void displayComments(List<Comment> comments) {
        cmtListSection.removeAllViews();

        for (Comment comment : comments) {
            View commentView = getLayoutInflater().inflate(R.layout.item_comment, cmtListSection, false);
            TextView authorTextView = commentView.findViewById(R.id.commentAuthor);
            TextView contentTextView = commentView.findViewById(R.id.commentText);
            authorTextView.setText(comment.getUser().getUsername());
            contentTextView.setText(comment.getContent());
            cmtListSection.addView(commentView);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        String bookId = getIntent().getStringExtra("BOOK_ID");
        fetchComments(bookId);
    }
}
