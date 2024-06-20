package com.example.mobileproject.dto.response;

import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Category;
import com.example.mobileproject.model.Comment;

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
    List<Comment> comments;
    double price;
    private int pages;
    String imageUrl;
    int status;
    private String content;
}
