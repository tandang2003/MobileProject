package com.example.mobileproject.dto.response;

import lombok.*;

import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long id;
    String content;
    UserResponse user;
}
