package pl.edu.uj.system_identity_management.controllers.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class AccessRequestBody {
    private Long userId;
    private String url;
    private String login;
    private String password;
}
