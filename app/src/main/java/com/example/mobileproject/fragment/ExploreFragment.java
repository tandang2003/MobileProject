package com.example.mobileproject.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.BookAdapter;
import com.example.mobileproject.activity.BookDetailActivity;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiCategory;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.AuthorResponse;
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

public class ExploreFragment extends Fragment {

    private BookAdapter adapter;
    private LinearLayout categoryLayout;
    private LinearLayout booksListSection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_page, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        booksListSection = view.findViewById(R.id.books_list_section);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new BookAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        categoryLayout = view.findViewById(R.id.category_layout);

        // Api lấy danh sách các cuốn sách
        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = convertToBookList(response.body().getResult());
                    displayBooks(books);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
                // Xử lý lỗi khi gọi API sách
                Toast.makeText(getContext(), "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });

        // Gọi API để lấy danh sách sách mới nhất, hiển thị ảnh
        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = convertToBookList(response.body().getResult());
                    adapter.setBooks(books);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
                // Xử lý lỗi khi gọi API sách
                Toast.makeText(getContext(), "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });

        // Gọi API để lấy danh sách danh mục
        ApiService.apiService.create(ApiCategory.class).getCategories().enqueue(new Callback<ApiResponse<List<CategoryResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryResponse>>> call, Response<ApiResponse<List<CategoryResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Set<String> categories = new HashSet<>();
                    for (CategoryResponse categoryResponse : response.body().getResult()) {
                        categories.add(categoryResponse.getName());
                    }
                    displayCategories(categories);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryResponse>>> call, Throwable throwable) {
                // Xử lý lỗi khi gọi API danh mục
                Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int center = recyclerView.getWidth() / 2;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    int childCenter = (viewHolder.itemView.getLeft() + viewHolder.itemView.getRight()) / 2;
                    float scale = 1.0f - Math.abs(center - childCenter) / (float) (center * 2);
                    viewHolder.itemView.setScaleX(scale + 0.5f);
                    viewHolder.itemView.setScaleY(scale + 0.5f);
                }
            }
        });

        return view;
    }

    private List<Book> convertToBookList(List<BookResponse> bookResponses) {
        List<Book> books = new ArrayList<>();
        for (BookResponse bookResponse : bookResponses) {
            Book book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setImageUrl(bookResponse.getImageUrl());

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

    private void displayCategories(Set<String> categories) {
        categoryLayout.removeAllViews();

        for (String category : categories) {
            Button button = new Button(getContext());
            button.setText(category);

            // Tạo margin cho các button
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 30, 0);
            button.setLayoutParams(layoutParams);

            // Tạo một background drawable tùy chỉnh
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(getResources().getColor(R.color.button));
            button.setBackground(drawable);

            // Thiết lập màu chữ cho button
            button.setTextColor(getResources().getColor(R.color.white));

            // Đặt căn giữa cho chữ trong button
            button.setGravity(Gravity.CENTER);

            // Đặt padding cho button
            int paddingInDp = 10;  // Đổi giá trị này nếu bạn muốn padding khác
            final float scale = getResources().getDisplayMetrics().density;
            int paddingInPixels = (int) (paddingInDp * scale + 0.5f);
            button.setPadding(paddingInPixels, 0, paddingInPixels, 0);

            button.setOnClickListener(v -> {
                // Xử lý khi nhấn vào nút danh mục
                Toast.makeText(getContext(), "Selected category: " + category, Toast.LENGTH_SHORT).show();
            });
            categoryLayout.addView(button);
        }
    }

    private void displayBooks(List<Book> books) {
        booksListSection.removeAllViews();

        for (Book book : books) {
            LinearLayout bookLayout = new LinearLayout(getContext());
            bookLayout.setOrientation(LinearLayout.HORIZONTAL);
            bookLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            bookLayout.setPadding(0, 0, 0, 32);

            ImageView bookImage = new ImageView(getContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    200, // width lớn hơn
                    250  // height lớn hơn
            );
            bookImage.setLayoutParams(imageParams);
            Picasso.get().load(book.getImageUrl()).into(bookImage);

            // Thêm sự kiện click vào ảnh
            bookImage.setOnClickListener(v -> {
                Log.d("ExploreFragment", "Image clicked for book: " + book.getTitle()); // Sử dụng Log.d để kiểm tra click event
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("BOOK_TITLE", book.getTitle());
                intent.putExtra("BOOK_AUTHOR", book.getAuthors().get(0).getName()); // Assuming there's at least one author
                intent.putExtra("BOOK_DESCRIPTION", "About this book..."); // Replace with actual description
                intent.putExtra("BOOK_IMAGE_URL", book.getImageUrl());
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

            optionsLayout.addView(optionsIcon);

            bookLayout.addView(bookImage);
            bookLayout.addView(bookInfoLayout);
            bookLayout.addView(optionsLayout);

            booksListSection.addView(bookLayout);
        }
    }
}
