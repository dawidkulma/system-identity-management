package pl.edu.uj.system_identity_management.controllers.access.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class AuthenticationRequestBody {
    private String url;
    private String login;
    private String password;
}
