package com.example.mobileproject.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    String title;
    List<Author> authors;
    List<Category> categories;
    String imageUrl;
}
