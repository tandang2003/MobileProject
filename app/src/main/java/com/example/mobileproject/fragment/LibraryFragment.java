package com.example.mobileproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.BookDetailActivity;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dialog.BookOptionsDialog;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Category;
import com.example.mobileproject.sharedPreference.GetData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryFragment extends Fragment {
    private static final String TAG = "LibraryFragment";
    private LinearLayout booksListSection;
    private ApiBook apiBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        booksListSection = rootView.findViewById(R.id.books_list_section);

        // Initialize shared preferences
        GetData.init(getContext());

        // Initialize Retrofit with token interceptor
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        String token = GetData.getInstance().getToken();
                        if (token != null && !token.isEmpty()) {
                            builder.header("Authorization", "Bearer " + token);
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/ebook/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiBook = retrofit.create(ApiBook.class);

        loadWishlist();

        return rootView;
    }

    private void loadWishlist() {
        String token = GetData.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(getActivity(), "No token found. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Fetching wishlist with token: " + token);

        Call<ApiResponse<List<BookResponse>>> call = apiBook.getBooksInWishList();
        call.enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, retrofit2.Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookResponse> wishlist = response.body().getResult();
                    displayWishlist(wishlist);
                } else {
                    try {
                        Log.e(TAG, "Failed to fetch wishlist: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Failed to fetch wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch wishlist", t);
                Toast.makeText(getActivity(), "Failed to fetch wishlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayWishlist(List<BookResponse> wishlist) {
        booksListSection.removeAllViews();
        for (BookResponse book : wishlist) {
            LinearLayout bookLayout = new LinearLayout(getContext());
            bookLayout.setOrientation(LinearLayout.HORIZONTAL);
            bookLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            bookLayout.setPadding(0, 0, 0, 32);

            ImageView bookImage = new ImageView(getContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    200, // width
                    250  // height
            );
            bookImage.setLayoutParams(imageParams);
            Picasso.get().load(book.getImageUrl()).into(bookImage);

            // Click listener for book image
            bookImage.setOnClickListener(v -> {
                Log.d(TAG, "Image clicked for book: " + book.getTitle());
                Log.d(TAG, "Book id: " + book.getId());
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("BOOK_TITLE", book.getTitle());
                intent.putExtra("BOOK_AUTHOR", book.getAuthors().get(0).getName());
                intent.putExtra("BOOK_CONTENT", book.getContent());
                intent.putExtra("BOOK_IMAGE_URL", book.getImageUrl());
                intent.putExtra("BOOK_ID", String.valueOf(book.getId()));
                startActivity(intent);
            });

            LinearLayout bookInfoLayout = new LinearLayout(getContext());
            bookInfoLayout.setOrientation(LinearLayout.VERTICAL);
            bookInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            bookInfoLayout.setPadding(16, 0, 0, 0);

            TextView bookTitle = new TextView(getContext());
            bookTitle.setText(book.getTitle());
            bookTitle.setTextColor(getResources().getColor(R.color.white));
            bookTitle.setTextSize(16);
            bookTitle.setPadding(0, 0, 0, 50);
            bookTitle.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout categoryLayout = new LinearLayout(getContext());
            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
            categoryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            for (Category category : book.getCategories()) {
                TextView categoryTextView = new TextView(getContext());
                categoryTextView.setText(category.getName());
                categoryTextView.setTextColor(getResources().getColor(R.color.white));
                categoryTextView.setPadding(4, 0, 4, 40);
                categoryTextView.setTextSize(14);
                categoryLayout.addView(categoryTextView);
            }

            LinearLayout authorLayout = new LinearLayout(getContext());
            authorLayout.setOrientation(LinearLayout.HORIZONTAL);
            authorLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            for (Author author : book.getAuthors()) {
                TextView authorTextView = new TextView(getContext());
                authorTextView.setText("Author: " + author.getName());
                authorTextView.setTextColor(getResources().getColor(R.color.white));
                authorTextView.setPadding(4, 0, 4, 0);
                authorTextView.setTextSize(14);
                authorLayout.addView(authorTextView);
            }

            bookInfoLayout.addView(bookTitle);
            bookInfoLayout.addView(categoryLayout);
            bookInfoLayout.addView(authorLayout);

            LinearLayout optionsLayout = new LinearLayout(getContext());
            optionsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            optionsLayout.setGravity(Gravity.CENTER_VERTICAL);

            ImageView optionsIcon = new ImageView(getContext());
            optionsIcon.setImageResource(R.drawable.ellipsis_vertical_solid);
            optionsIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    100,
                    50
            ));
            optionsIcon.setOnClickListener(v -> {
                // Khi người dùng nhấp vào một cuốn sách
                BookOptionsDialog dialog = new BookOptionsDialog(
                        getContext(),
                        book.getTitle(),
                        book.getAuthors().get(0).getName(),
                        book.getImageUrl()
                );
                dialog.setOnOptionClickListener(new BookOptionsDialog.OnOptionClickListener() {
                    @Override
                    public void onAboutClicked() {
                        // Handle About option
                        Intent intent = new Intent(getContext(), BookDetailActivity.class);
                        intent.putExtra("BOOK_TITLE", book.getTitle());
                        intent.putExtra("BOOK_AUTHOR", book.getAuthors().get(0).getName());
                        intent.putExtra("BOOK_CONTENT", book.getContent());
                        intent.putExtra("BOOK_IMAGE_URL", book.getImageUrl());
                        intent.putExtra("BOOK_ID", String.valueOf(book.getId()));
                        startActivity(intent);
                    }

                    @Override
                    public void onLikeClicked() {
                        // No need to check, directly unlike the book
                        removeFromWishlist(book.getId());
                    }

                    public void removeFromWishlist(long bookId) {
                        String token = GetData.getInstance().getToken();
                        if (token == null || token.isEmpty()) {
                            // Handle case where no token is found
                            Toast.makeText(getActivity(), "No token found. Please log in.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d(TAG, "Removing book from wishlist with token: " + token);

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


                        ApiBook apiBook = ApiService.apiService.newBuilder()
                                .client(client)
                                .build()
                                .create(ApiBook.class);

                        Call<Void> call = apiBook.removeFromWishlist(bookId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // Handle successful response
                                    Log.d(TAG, "Book removed from wishlist: " + bookId);
                                    loadWishlist();
                                } else {
                                    // Handle case where response is not successful
                                    try {
                                        Log.e(TAG, "Failed to remove book from wishlist: " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getActivity(), "Failed to remove book from wishlist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Handle case where request fails
                                Log.e(TAG, "Failed to remove book from wishlist", t);
                                Toast.makeText(getActivity(), "Failed to remove book from wishlist", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onDownloadClicked() {
                        // Handle Download option
                    }
                });
                dialog.show();
            });


            optionsLayout.addView(optionsIcon);

            bookLayout.addView(bookImage);
            bookLayout.addView(bookInfoLayout);
            bookLayout.addView(optionsLayout);

            booksListSection.addView(bookLayout);
        }
    }
}
