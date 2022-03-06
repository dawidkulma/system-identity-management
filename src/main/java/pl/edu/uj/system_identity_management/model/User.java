package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Boolean isBlocked;
}