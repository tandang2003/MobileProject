package edu.vn.hcmuaf.ebook.dto.response;

import edu.vn.hcmuaf.ebook.entity.Author;
import edu.vn.hcmuaf.ebook.entity.Category;
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
    List<Author> authors;
    List<Category> categories;
    String imageUrl;
}
