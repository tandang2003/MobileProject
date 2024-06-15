package com.example.mobileproject.dto.response;

import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    long id;
    String title;
    List<Author> authors;
    List<Category> categories;
    List<CommentResponse> comments;
    double price;
    private int pages;
    String imageUrl;
}
