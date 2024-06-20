package com.example.mobileproject.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Book {
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
