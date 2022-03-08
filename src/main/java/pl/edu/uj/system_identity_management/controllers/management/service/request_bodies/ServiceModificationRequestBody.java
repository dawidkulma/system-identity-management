package pl.edu.uj.system_identity_management.controllers.management.service.request_bodies;

import lombok.*;

@Getter
@NoArgsConstructor
public class ServiceModificationRequestBody {
    private String currentUrl;
    private String newUrl;
}