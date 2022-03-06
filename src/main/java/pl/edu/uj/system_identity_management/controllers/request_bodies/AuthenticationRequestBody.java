package pl.edu.uj.system_identity_management.controllers.request_bodies;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthenticationRequestBody {
    private String url;
    private String login;
    private String password;
}
