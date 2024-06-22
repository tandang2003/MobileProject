package com.example.mobileproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dialog.comment.CommentDialog;
import com.example.mobileproject.dto.request.WishListRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.dto.response.CommentResponse;
import com.example.mobileproject.dto.response.WishListResponse;
import com.example.mobileproject.model.Comment;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Exception;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private ImageButton backButton,loveButton;
    private ImageView bookCover, moreOptionsButton;
    private TextView bookTitle, bookAuthor, bookContent, ratingTitle;
    private MaterialButton readButton, commentButton;
    private LinearLayout cmtListSection;
    private boolean isInWishlist = false;

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
        loveButton = findViewById(R.id.loveButton);

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

        fetchComments(bookId);
//        checkWishlistStatus();
        ApiService.apiService.create(ApiBook.class).getBooksInWishList().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookResponse> wishlist = response.body().getResult();
                    isInWishlist = wishlist.stream().anyMatch(book -> book.getId().equals(Long.parseLong(bookId)));
                    Log.d("BookDetailActivity", "isInWishlist: " + isInWishlist);
                    updateLoveButtonUI();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable t) {
                Log.e("BookDetailActivity", "Failed to check wishlist status", t);
            }
        });

        loveButton.setOnClickListener(v -> toggleWishlist());
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

//    wishlist

    private void toggleWishlist() {
        if (isInWishlist) {
            removeFromWishlist();
        } else {
            addToWishlist();
        }
    }

    private void addToWishlist() {
        String bookId = getIntent().getStringExtra("BOOK_ID");
        String token = GetData.getInstance().getToken();
        Log.d("BookDetailActivity", "Token: " + token);

        if (token == null || token.equals(Exception.NOT_FOUND_DATA.getMessage())) {
            // Handle case where user is not logged in
            Toast.makeText(this, "Please log in to add to wishlist", Toast.LENGTH_SHORT).show();
            return;
        }

        WishListRequest request = new WishListRequest();
        request.setBookId(Long.parseLong(bookId));

        ApiService.apiService.create(ApiBook.class).addToWishlist("Bearer " + token, request).enqueue(new Callback<ApiResponse<WishListResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<WishListResponse>> call, Response<ApiResponse<WishListResponse>> response) {
                if (response.isSuccessful()) {
                    isInWishlist = true;
                    Toast.makeText(BookDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    updateLoveButtonUI();
                } else {
                    Log.e("BookDetailActivity", "Failed to add to wishlist. Response code: " + response.code());
                    try {
                        Log.e("BookDetailActivity", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WishListResponse>> call, Throwable t) {
                Log.e("BookDetailActivity", "Failed to add to wishlist", t);
            }
        });
    }

    private void removeFromWishlist() {
        String bookId = getIntent().getStringExtra("BOOK_ID");
        ApiService.apiService.create(ApiBook.class).removeFromWishlist(Long.parseLong(bookId)).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    isInWishlist = false;
                    updateLoveButtonUI();
                    Toast.makeText(BookDetailActivity.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Log.e("BookDetailActivity", "Failed to remove from wishlist", t);
            }
        });
    }

    private void updateLoveButtonUI() {
        loveButton.setImageResource(isInWishlist ? R.drawable.heart_check_24px : R.drawable.like);
    }
}
