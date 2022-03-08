package pl.edu.uj.system_identity_management.controllers.management.user.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class GrantingAccessRequestBody {
    private Long userId;
    private String url;
    private String login;
    private String password;
}
