package pl.edu.uj.system_identity_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.uj.system_identity_management.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
