package pl.edu.uj.system_identity_management.controllers.response_bodies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAuthenticationsToService {
    private String url;
    private List<Date> dates;

    public UserAuthenticationsToService(String url) {
        this.url = url;
        this.dates = new ArrayList<>();
    }

    public void add(Date date) {
        this.dates.add(date);
    }
}
