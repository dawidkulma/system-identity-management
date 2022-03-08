package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Service")
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;

    public Service(String url) {
        this.url = url;
    }
}
