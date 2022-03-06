package pl.edu.uj.system_identity_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.uj.system_identity_management.model.Credential;
import pl.edu.uj.system_identity_management.model.Service;
import pl.edu.uj.system_identity_management.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByServiceAndLogin(Service service, String login);

    Optional<Credential> findByUserAndService(User user, Service service);

    List<Credential> findByUser(User user);
}
