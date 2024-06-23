package edu.vn.hcmuaf.ebook.dto.request;

import edu.vn.hcmuaf.ebook.entity.Author;
import edu.vn.hcmuaf.ebook.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    String title;
    double price;
    Set<Category> categories;
    Set<Author> authors;
    String content;
    String imageUrl;
    int pages;
}
