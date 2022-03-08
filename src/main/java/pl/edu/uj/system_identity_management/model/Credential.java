package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Credential")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private Boolean isBlocked;

    @Column(nullable = false)
    private Boolean isLoggedIn;

    @OneToMany(
            mappedBy = "credential",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AuthenticationDetail> authenticationDetails = new ArrayList<>();

    public void addAuthenticationDetail(AuthenticationDetail authenticationDetail) {
        authenticationDetails.add(authenticationDetail);
        authenticationDetail.setCredential(this);
    }

    public Credential(User user, Service service, String login, String hash) {
        this.user = user;
        this.service = service;
        this.login = login;
        this.hash = hash;
        this.isBlocked = false;
    }
}