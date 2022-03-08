package pl.edu.uj.system_identity_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select c.service from Credential c " +
            "where c.user = ?1 and c.isBlocked = false")
    List<Service> getServicesByUserWithPermissions(User user);

    @Query("select c from Credential c " +
            "where c.user.id = ?1 and c.service.url = ?2")
    Optional<Credential> findByUserIdAndServiceUrl(Long userId, String url);
}
