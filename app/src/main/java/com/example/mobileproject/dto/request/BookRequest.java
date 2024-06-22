package com.example.mobileproject.dto.request;

import com.example.mobileproject.model.Author;
import com.example.mobileproject.model.Category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    Long id;
    String title;
    double price;
    Set<Category> categories;
    Set<Author> authors;
    String content;
    String imageUrl;
    int pages;
}
