package pl.edu.uj.system_identity_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.uj.system_identity_management.Utils.PasswordManagement;
import pl.edu.uj.system_identity_management.controllers.request_bodies.AuthenticationRequestBody;
import pl.edu.uj.system_identity_management.controllers.request_bodies.AuthorizeRequestBody;
import pl.edu.uj.system_identity_management.model.Credential;
import pl.edu.uj.system_identity_management.model.Service;
import pl.edu.uj.system_identity_management.model.User;
import pl.edu.uj.system_identity_management.repositories.CredentialRepository;
import pl.edu.uj.system_identity_management.repositories.ServiceRepository;
import pl.edu.uj.system_identity_management.repositories.UserRepository;

import java.util.Optional;

@RestController
public class ExternalServicesController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @RequestMapping(value = "/autentykuj", method = RequestMethod.POST)
    public ResponseEntity authenticate(@RequestBody AuthenticationRequestBody requestBody) {
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        Service service = serviceOptional.get();
        Optional<Credential> credentialOptional = credentialRepository.findByServiceAndLogin(service, requestBody.getLogin());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Credential credential = credentialOptional.get();
        if (credential.getUser().getIsBlocked()) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (!PasswordManagement.check(requestBody.getPassword(), credential.getHash())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/autoryzuj", method = RequestMethod.POST)
    public ResponseEntity authorize(@RequestBody AuthorizeRequestBody requestBody) {
        Optional<User> userOptional = userRepository.findById(requestBody.getUserId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }
}
