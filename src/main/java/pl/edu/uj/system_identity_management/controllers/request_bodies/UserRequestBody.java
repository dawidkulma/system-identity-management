package pl.edu.uj.system_identity_management.controllers.request_bodies;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestBody {
    private Long id;
    private String name;
    private String surname;
}
