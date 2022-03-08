package pl.edu.uj.system_identity_management.controllers.management.user.response_bodies;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAuthenticationsDetailsResponseBody {
    private List<UserAuthenticationsToService> authentications;

    public UserAuthenticationsDetailsResponseBody() {
        this.authentications = new ArrayList<>();
    }

    public void add(UserAuthenticationsToService auths) {
        this.authentications.add(auths);
    }
}
