package com.example.mobileproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiCategory;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.dto.response.CategoryResponse;
import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Book;
import com.example.mobileproject.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerActivity extends AppCompatActivity {

    private BookAdapter adapter;
    private LinearLayout booksListSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_book);

        // Retrieve the button text from the Intent


        // Find the TextView and set its text
        TextView exploreText = findViewById(R.id.exploreText);
//        exploreText.setText(buttonText);

        // Setup the back button to navigate back to ExploreActivity
        ImageButton backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(RecyclerActivity.this, ExploreActivity.class);
//            startActivity(intent);
            finish();
        });

        booksListSection = findViewById(R.id.books_list_section);
        adapter = new BookAdapter(new ArrayList<>());
        Intent intent = getIntent();
        String buttonText = null;

        if (intent != null) {
            try {
                Bundle bundle =getIntent().getBundleExtra("BUTTON_TEXT");
                buttonText=bundle.getString("category");
                Log.d("RecyclerActivity", "buttonText: " + buttonText); // Log giá trị của buttonText
            } catch (ClassCastException e) {
                Long buttonLong = intent.getLongExtra("BUTTON_TEXT", -1L);
                if (buttonLong != -1L) {
                    buttonText = String.valueOf(buttonLong);
                    Log.d("RecyclerActivity", "Converted buttonLong to buttonText: " + buttonText); // Log giá trị của buttonLong sau khi chuyển đổi
                }
            }
        }

        if (buttonText != null && !buttonText.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(buttonText);
                Log.d("RecyclerActivity", "categoryId: " + categoryId); // Log giá trị của categoryId
                ApiService.apiService.create(ApiCategory.class).getCategoryById(categoryId).enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BookResponse> bookResponses = response.body().getResult();
                            List<Book> books = convertToBookList(bookResponses);
                            displayBooks(books);
                        } else {
                            Toast.makeText(RecyclerActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
                        Toast.makeText(RecyclerActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Log.e("RecyclerActivity", "Invalid category ID: " + buttonText, e);
                Toast.makeText(RecyclerActivity.this, "Invalid category ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("RecyclerActivity", "Category ID is empty or null: " + buttonText);
            Toast.makeText(RecyclerActivity.this, "Category ID is empty or null", Toast.LENGTH_SHORT).show();
        }


        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>> () {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = convertToBookList(response.body().getResult());
                    adapter.setBooks(books);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
                Toast.makeText(RecyclerActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });

        ApiService.apiService.create(ApiCategory.class).getCategories().enqueue(new Callback<ApiResponse<List<CategoryResponse>>> () {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryResponse>>> call, Response<ApiResponse<List<CategoryResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Set<String> categories = new HashSet<>();
                    for (CategoryResponse categoryResponse : response.body().getResult()) {
                        categories.add(categoryResponse.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryResponse>>> call, Throwable throwable) {
                Toast.makeText(RecyclerActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Book> convertToBookList(List<BookResponse> bookResponses) {
        List<Book> books = new ArrayList<>();
        for (BookResponse bookResponse : bookResponses) {
            Book book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setImageUrl(bookResponse.getImageUrl());

            List<Category> categories = new ArrayList<>();
            for (Category categoryResponse : book.getCategories()) {
                Category category = new Category();
                System.out.println(category.getName());
                category.setName(categoryResponse.getName());
                categories.add(category);
            }
            book.setCategories(categories);

            List<Author> authors = new ArrayList<>();
            for (Author authorResponse : book.getAuthors()) {
                Author author = new Author();
                author.setName(authorResponse.getName());
                authors.add(author);
            }
            book.setAuthors(authors);

            books.add(book);
        }
        return books;
    }

    private void displayBooks(List<Book> books) {
        LinearLayout booksListSection = findViewById(R.id.books_list_section);
        booksListSection.removeAllViews();

        for (Book book : books) {
            LinearLayout bookLayout = new LinearLayout(this);
            bookLayout.setOrientation(LinearLayout.HORIZONTAL);
            bookLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            bookLayout.setPadding(0, 0, 0, 32);

            ImageView bookImage = new ImageView(this);
            // Thay đổi kích thước của hình ảnh
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    250, // width lớn hơn
                    300  // height lớn hơn
            );
            bookImage.setLayoutParams(imageParams);
            Picasso.get().load(book.getImageUrl()).into(bookImage);

            LinearLayout bookInfoLayout = new LinearLayout(this);
            bookInfoLayout.setOrientation(LinearLayout.VERTICAL);
            bookInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            bookInfoLayout.setPadding(5, 0, 0, 0);

            TextView bookTitle = new TextView(this);
            bookTitle.setText(book.getTitle());
            bookTitle.setTextColor(getResources().getColor(R.color.white));
            bookTitle.setTextSize(16);
            bookTitle.setPadding(0, 0, 0, 10);
            bookTitle.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout categoryLayout = new LinearLayout(this);
            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
            categoryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            for (Category category : book.getCategories()) {
                TextView categoryTextView = new TextView(this);
                System.out.println("Danh muc:" + category.getName());
                categoryTextView.setText(category.getName());
                categoryTextView.setTextColor(getResources().getColor(R.color.white));
                categoryTextView.setPadding(4, 0, 4, 10);
                categoryTextView.setTextSize(14);
                categoryLayout.addView(categoryTextView);
            }

            LinearLayout authorLayout = new LinearLayout(this);
            authorLayout.setOrientation(LinearLayout.HORIZONTAL);
            authorLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            for (Author author : book.getAuthors()) {
                TextView authorTextView = new TextView(this);
                authorTextView.setText("Author: " + author.getName());
                authorTextView.setTextColor(getResources().getColor(R.color.white));
                authorTextView.setPadding(4, 0, 4, 0);
                authorTextView.setTextSize(14);
                authorLayout.addView(authorTextView);
            }

            bookInfoLayout.addView(bookTitle);
            bookInfoLayout.addView(categoryLayout);
            bookInfoLayout.addView(authorLayout);

            LinearLayout optionsLayout = new LinearLayout(this);
            optionsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            optionsLayout.setGravity(Gravity.CENTER_VERTICAL);

            ImageView optionsIcon = new ImageView(this);
            optionsIcon.setImageResource(R.drawable.ellipsis_vertical_solid);
            optionsIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    100,
                    50
            ));

            optionsLayout.addView(optionsIcon);

            bookLayout.addView(bookImage);
            bookLayout.addView(bookInfoLayout);
            bookLayout.addView(optionsLayout);

            booksListSection.addView(bookLayout);
        }
    }

//    private List<Book> filterBooksByCategory(List<Book> books, String category) {
//        List<Book> filteredBooks = new ArrayList<>();
//        for (Book book : books) {
//            for (Category bookCategory : book.getCategories()) {
//                if (bookCategory.getName().equals(category)) {
//                    filteredBooks.add(book);
//                    break;
//                }
//            }
//        }
//        return filteredBooks;
//    }
}
