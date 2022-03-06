package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Credentials")
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String hash;

    @OneToMany
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private List<AuthenticationDetail> authenticationDetails;

    public Credential(User user, Service service, String login, String hash) {
        this.user = user;
        this.service = service;
        this.login = login;
        this.hash = hash;
    }
}