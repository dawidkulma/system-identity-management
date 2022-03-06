package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Services")
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;
}
