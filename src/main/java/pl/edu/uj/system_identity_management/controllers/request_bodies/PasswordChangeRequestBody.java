package pl.edu.uj.system_identity_management.controllers.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class PasswordChangeRequestBody {
    private Long userId;
    private String url;
    private String currentPass;
    private String newPass;
}
