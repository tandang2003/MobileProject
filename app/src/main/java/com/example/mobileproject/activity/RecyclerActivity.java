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

        // Find the TextView and set its text
        TextView exploreText = findViewById(R.id.exploreText);

        // Setup the back button to navigate back to ExploreActivity
        ImageButton backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        booksListSection = findViewById(R.id.books_list_section);
        adapter = new BookAdapter(new ArrayList<>());
        Intent intent = getIntent();
        int categoryId = -1;
        String categoryName = "";

        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("CATEGORY_BUNDLE");
            if (bundle != null) {
                categoryId = Integer.parseInt(bundle.getString("category_id"));
                categoryName = bundle.getString("category_name");
            }
        }

        if (categoryId != -1) {
            exploreText.setText(categoryName);
            fetchBooksByCategory(categoryId);
        } else {
            Toast.makeText(this, "Invalid category ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchBooksByCategory(int categoryId) {
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
    }

    private List<Book> convertToBookList(List<BookResponse> bookResponses) {
        List<Book> books = new ArrayList<>();
        for (BookResponse bookResponse : bookResponses) {
            Book book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setContent(bookResponse.getContent());
            book.setImageUrl(bookResponse.getImageUrl());
            book.setId(bookResponse.getId());

            List<Category> categories = new ArrayList<>();
            for (Category categoryResponse : bookResponse.getCategories()) {
                Category category = new Category();
                category.setName(categoryResponse.getName());
                categories.add(category);
            }
            book.setCategories(categories);

            List<Author> authors = new ArrayList<>();
            for (Author authorResponse : bookResponse.getAuthors()) {
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
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    200, // width lớn hơn
                    250  // height lớn hơn
            );
            bookImage.setLayoutParams(imageParams);
            Picasso.get().load(book.getImageUrl()).into(bookImage);

            // Thêm sự kiện click vào ảnh
            bookImage.setOnClickListener(v -> {
                Log.d("RecyclerActivity", "Image clicked for book: " + book.getTitle());
                Log.d("RecyclerActivity", "Book id: " + book.getId());
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("BOOK_TITLE", book.getTitle());
                intent.putExtra("BOOK_AUTHOR", book.getAuthors().get(0).getName());
                intent.putExtra("BOOK_CONTENT", book.getContent());
                intent.putExtra("BOOK_IMAGE_URL", book.getImageUrl());
                intent.putExtra("BOOK_ID", String.valueOf(book.getId()));
                startActivity(intent);
            });

            LinearLayout bookInfoLayout = new LinearLayout(this);
            bookInfoLayout.setOrientation(LinearLayout.VERTICAL);
            bookInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            bookInfoLayout.setPadding(16, 0, 0, 0);

            TextView bookTitle = new TextView(this);
            bookTitle.setText(book.getTitle());
            bookTitle.setTextColor(getResources().getColor(R.color.white));
            bookTitle.setTextSize(16);
            bookTitle.setPadding(0, 0, 0, 50);
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
                categoryTextView.setText(category.getName());
                categoryTextView.setTextColor(getResources().getColor(R.color.white));
                categoryTextView.setPadding(4, 0, 4, 40);
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
                authorTextView.setText(author.getName());
                authorTextView.setTextColor(getResources().getColor(R.color.white));
                authorTextView.setPadding(4, 0, 4, 0);
                authorTextView.setTextSize(14);
                authorLayout.addView(authorTextView);
            }

            bookInfoLayout.addView(bookTitle);
            bookInfoLayout.addView(categoryLayout);
            bookInfoLayout.addView(authorLayout);

            bookLayout.addView(bookImage);
            bookLayout.addView(bookInfoLayout);

            booksListSection.addView(bookLayout);
        }
    }

}
