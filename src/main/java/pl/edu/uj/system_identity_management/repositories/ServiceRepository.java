package pl.edu.uj.system_identity_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.uj.system_identity_management.model.Service;
import pl.edu.uj.system_identity_management.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByUrl(String url);

    @Query("select s from Services s " +
            "left join Credentials c on s = c.service " +
            "where c.user = ?1")
    List<Service> getServicesByUser(User user);
}
