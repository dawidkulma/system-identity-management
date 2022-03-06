package pl.edu.uj.system_identity_management.controllers.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class ServiceRequestBody {
    private Long id;
    private String url;
}
