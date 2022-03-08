package pl.edu.uj.system_identity_management.controllers.management.user.response_bodies;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserAuthenticationsToService {
    private String url;
    private List<String> dates;

    public UserAuthenticationsToService(String url) {
        this.url = url;
        this.dates = new ArrayList<>();
    }

    public void add(String date) {
        this.dates.add(date);
    }
}
