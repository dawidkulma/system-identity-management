package pl.edu.uj.system_identity_management.controllers.management.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.uj.system_identity_management.Utils.PasswordManager;
import pl.edu.uj.system_identity_management.controllers.management.user.request_bodies.*;
import pl.edu.uj.system_identity_management.controllers.management.user.response_bodies.UserAuthenticationsDetailsResponseBody;
import pl.edu.uj.system_identity_management.controllers.management.user.response_bodies.UserAuthenticationsToService;
import pl.edu.uj.system_identity_management.model.*;
import pl.edu.uj.system_identity_management.repositories.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @RequestMapping(value = "/uzytkownik-dodaj", method = RequestMethod.POST)
    public ResponseEntity<User> addUser(@RequestBody UserCreationRequestBody requestBody) {
        User newUser = new User(requestBody.getName(), requestBody.getSurname());
        userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-info", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.isPresent() ? new ResponseEntity<>(userOptional.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/uzytkownik-modyfikuj", method = RequestMethod.PUT)
    public ResponseEntity modifyUser(@RequestBody UserModificationRequestBody requestBody) {
        Optional<User> userOptional = userRepository.findById(requestBody.getId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        User userToModify = userOptional.get();
        userToModify.setName(requestBody.getName());
        userToModify.setSurname(requestBody.getSurname());
        userRepository.save(userToModify);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-lista", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-przyznaj-dostep-do-serwisu", method = RequestMethod.POST)
    public ResponseEntity grantAccessToService(@RequestBody GrantingAccessRequestBody requestBody ) {
        Optional<User> userOptional = userRepository.findById(requestBody.getUserId());
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (userOptional.isEmpty() || serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Optional<Credential> credentialOptional = credentialRepository.findByServiceAndLogin(serviceOptional.get(), requestBody.getLogin());
        if (credentialOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        Credential credential = new Credential(
                userOptional.get(),
                serviceOptional.get(),
                requestBody.getLogin(),
                PasswordManager.generateHash(requestBody.getPassword()));
        credentialRepository.save(credential);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-odbierz-dostep-do-serwisu", method = RequestMethod.PUT)
    public ResponseEntity revokeAccessToService(@RequestBody RevokingAccessRequestBody requestBody) {
        Optional<User> userOptional = userRepository.findById(requestBody.getUserId());
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (userOptional.isEmpty() || serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Optional<Credential> credentialOptional = credentialRepository.findByUserAndService(userOptional.get(), serviceOptional.get());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        credentialRepository.delete(credentialOptional.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-uprawnienia", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getUserServices(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(credentialRepository.getServicesByUserWithPermissions(userOptional.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-zmiana-hasla", method = RequestMethod.PUT)
    public ResponseEntity changeUserPasswordForService(@RequestBody PasswordChangeRequestBody requestBody) {
        Optional<Credential> credentialOptional = credentialRepository.findByUserIdAndServiceUrl(requestBody.getUserId(), requestBody.getUrl());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Credential userCredential = credentialOptional.get();
        if (!PasswordManager.check(requestBody.getCurrentPass(), userCredential.getHash())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        userCredential.setHash(PasswordManager.generateHash(requestBody.getNewPass()));
        credentialRepository.save(userCredential);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-zablokuj", method = RequestMethod.PUT)
    public ResponseEntity blockUser(@RequestBody BlockingRequestBody requestBody) {
        Optional<Credential> credentialOptional = credentialRepository.findByUserIdAndServiceUrl(requestBody.getUserId(), requestBody.getUrl());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Credential credentialToBlock = credentialOptional.get();
        credentialToBlock.setIsBlocked(Boolean.TRUE);
        credentialRepository.save(credentialToBlock);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-autentykacje", method = RequestMethod.GET)
    public ResponseEntity<UserAuthenticationsDetailsResponseBody> getUserAuthentications(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        UserAuthenticationsDetailsResponseBody responseBody = new UserAuthenticationsDetailsResponseBody();
        for (Credential c: credentialRepository.findByUser(user)) {
            UserAuthenticationsToService auths = new UserAuthenticationsToService(c.getService().getUrl());
            for (AuthenticationDetail ad: c.getAuthenticationDetails()) {
                auths.add(ad.getDate());
            }
            responseBody.add(auths);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}