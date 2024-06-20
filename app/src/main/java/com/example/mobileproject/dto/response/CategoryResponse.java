package com.example.mobileproject.dto.response;

import com.example.mobileproject.model.Book;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    long id;
    String name;
    private List<Book> books;
}
