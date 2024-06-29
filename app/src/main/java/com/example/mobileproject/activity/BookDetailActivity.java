package com.example.mobileproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.mobileproject.model.Comment;
import com.example.mobileproject.sharedPreference.GetData;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView bookCover, moreOptionsButton;
    private TextView bookTitle, bookAuthor, bookContent, ratingTitle;
    private MaterialButton readButton, commentButton;
    private LinearLayout cmtListSection;
    private PopupWindow popupWindow;

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
        Picasso.get().load(imageUrl).into(bookCover);

        // Set click listeners
        backButton.setOnClickListener(v -> onBackPressed());

        commentButton.setOnClickListener(v -> showCommentDialog(bookId));
        readButton.setOnClickListener(v -> {
            Intent intent = new Intent(BookDetailActivity.this, ReadingActivity.class);
            intent.putExtra("BOOK_CONTENT", content);
            intent.putExtra("BOOK_TITLE", bookTitle.getText().toString());
            startActivity(intent);
        });

        fetchComments(bookId);

        ImageView moreOptionsButton = findViewById(R.id.imageButton2);
        moreOptionsButton.setOnClickListener(v -> showOptionsPopup(v));
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

    private void showOptionsPopup(View anchorView) {
        // Create the options layout
        LinearLayout optionsLayout = new LinearLayout(this);
        optionsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        optionsLayout.setOrientation(LinearLayout.VERTICAL);
        optionsLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        optionsLayout.setPadding(20, 20, 20, 20);

        // Create and add option buttons

        String[] options = {"Like", "Download"};
        for (String option : options) {
            MaterialButton button = new MaterialButton(this);
            button.setText(option);
            button.setOnClickListener(v -> {
                if (option.equals("Like")) {
                    addToWishlist();
                } else if (option.equals("Download")) {
                    // Handle download action
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            });
            optionsLayout.addView(button);
        }

        // Create and show the popup window
        popupWindow = new PopupWindow(
                optionsLayout,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // Show the popup window
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    private void addToWishlist() {
        String bookId = getIntent().getStringExtra("BOOK_ID");

        if (bookId == null) {
            Log.e("BookDetailActivity", "Book ID is null");
            return;
        }

        WishListRequest request = new WishListRequest(Long.parseLong(bookId));

        // Get the token from GetData
        String token = GetData.getInstance().getToken();

        // Create a new OkHttpClient with an interceptor to add the token
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .method(original.method(), original.body());
                    Request newRequest = requestBuilder.build();
                    return chain.proceed(newRequest);
                })
                .build();

        // Create the API service using the existing ApiService interface
        ApiBook apiBook = ApiService.apiService.newBuilder()
                .client(client)
                .build()
                .create(ApiBook.class);

        // Make the API call
        apiBook.addToWishlist(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(BookDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(BookDetailActivity.this, "Failed to add to wishlist", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(BookDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                });
                Log.e("BookDetailActivity", "Network error", t);
            }
        });
    }
}
