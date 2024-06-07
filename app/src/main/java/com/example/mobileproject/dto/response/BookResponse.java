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
    String title;
    private List<CategoryResponse> categories;
    private List<AuthorResponse> authors;
    String imageUrl;
}
