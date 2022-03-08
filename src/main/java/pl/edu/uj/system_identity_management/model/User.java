package pl.edu.uj.system_identity_management.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}