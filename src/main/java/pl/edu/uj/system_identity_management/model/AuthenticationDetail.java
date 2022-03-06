package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "authentcations_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "credential_id", nullable = false)
    private Long credentialId;

    @Column(nullable = false)
    private Date date;
}
