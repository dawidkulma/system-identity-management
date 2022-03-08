package pl.edu.uj.system_identity_management.controllers.access.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class AuthorizeRequestBody {
    private Long userId;
    private String url;
}
