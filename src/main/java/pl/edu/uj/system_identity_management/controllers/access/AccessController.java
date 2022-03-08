package pl.edu.uj.system_identity_management.controllers.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.uj.system_identity_management.Utils.PasswordManager;
import pl.edu.uj.system_identity_management.controllers.access.request_bodies.*;
import pl.edu.uj.system_identity_management.model.*;
import pl.edu.uj.system_identity_management.repositories.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
public class AccessController {

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
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Service service = serviceOptional.get();
        Optional<Credential> credentialOptional = credentialRepository.findByServiceAndLogin(service, requestBody.getLogin());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Credential credential = credentialOptional.get();
        if (credential.getIsBlocked()) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (!PasswordManager.check(requestBody.getPassword(), credential.getHash())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        credential.setIsLoggedIn(Boolean.TRUE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        credential.addAuthenticationDetail(new AuthenticationDetail(dateFormat.format(date)));
        credentialRepository.save(credential);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/autoryzuj", method = RequestMethod.POST)
    public ResponseEntity authorize(@RequestBody AuthorizeRequestBody requestBody) {
        Optional<Credential> credentialOptional = credentialRepository.findByUserIdAndServiceUrl(requestBody.getUserId(), requestBody.getUrl());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Credential credential = credentialOptional.get();
        if (credential.getIsLoggedIn()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
