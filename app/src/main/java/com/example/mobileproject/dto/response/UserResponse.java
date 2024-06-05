package edu.vn.hcmuaf.ebook.dto.response;

import edu.vn.hcmuaf.ebook.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String fullName;
    String email;
    String avatar;
    Role role;
}
