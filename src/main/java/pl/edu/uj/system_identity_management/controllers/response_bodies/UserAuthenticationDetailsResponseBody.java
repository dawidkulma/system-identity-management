package pl.edu.uj.system_identity_management.controllers.response_bodies;

import java.util.ArrayList;
import java.util.List;

public class UserAuthenticationDetailsResponseBody {
    private List<UserAuthenticationsToService> authentications;

    public UserAuthenticationDetailsResponseBody() {
        this.authentications = new ArrayList<>();
    }

    public void add(UserAuthenticationsToService auths) {
        this.authentications.add(auths);
    }
}
