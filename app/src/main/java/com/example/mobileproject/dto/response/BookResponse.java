package edu.vn.hcmuaf.ebook.dto.response;

import edu.vn.hcmuaf.ebook.entity.Author;
import edu.vn.hcmuaf.ebook.entity.Category;
import edu.vn.hcmuaf.ebook.entity.Comment;
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
    List<CategoryResponse> categories;
    List<CommentResponse> comments;
    double price;
    private int pages;
    String imageUrl;
    int status;
    private String content;

}
