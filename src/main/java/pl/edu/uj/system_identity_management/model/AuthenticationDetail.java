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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @Column(nullable = false)
    private String date;

    public AuthenticationDetail(String date) {
        this.date = date;
    }
}
