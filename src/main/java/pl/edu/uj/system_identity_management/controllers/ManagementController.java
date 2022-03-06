package pl.edu.uj.system_identity_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.uj.system_identity_management.Utils.PasswordManagement;
import pl.edu.uj.system_identity_management.controllers.request_bodies.*;
import pl.edu.uj.system_identity_management.controllers.response_bodies.UserAuthenticationDetailsResponseBody;
import pl.edu.uj.system_identity_management.controllers.response_bodies.UserAuthenticationsToService;
import pl.edu.uj.system_identity_management.model.AuthenticationDetail;
import pl.edu.uj.system_identity_management.model.Credential;
import pl.edu.uj.system_identity_management.model.Service;
import pl.edu.uj.system_identity_management.model.User;
import pl.edu.uj.system_identity_management.repositories.CredentialRepository;
import pl.edu.uj.system_identity_management.repositories.ServiceRepository;
import pl.edu.uj.system_identity_management.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ManagementController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @RequestMapping(value = "/serwis-dodaj", method = RequestMethod.POST)
    public ResponseEntity addService(@RequestBody ServiceRequestBody requestBody) {
        serviceRepository.save(new Service(requestBody.getId(), requestBody.getUrl()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/serwis-modyfikuj", method = RequestMethod.PUT)
    public ResponseEntity modifyService(@RequestBody ServiceRequestBody requestBody) {
        Optional<Service> opt = serviceRepository.findById(requestBody.getId());
        if (opt.isPresent()) {
            Service serviceToModify = opt.get();
            serviceToModify.setUrl(requestBody.getUrl());
            serviceRepository.save(serviceToModify);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/serwis-usun", method = RequestMethod.DELETE)
    public ResponseEntity deleteService(@RequestBody ServiceRequestBody requestBody) {
        Optional<Service> opt = serviceRepository.findById(requestBody.getId());
        if (opt.isPresent()) {
            Service serviceToDelete = opt.get();
            serviceRepository.delete(serviceToDelete);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/serwis-lista", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getServices() {
        return new ResponseEntity<>(serviceRepository.findAll(), HttpStatus.OK);
    }



    @RequestMapping(value = "/uzytkownik-dodaj", method = RequestMethod.POST)
    public ResponseEntity addUser(@RequestBody UserRequestBody requestBody) {
        userRepository.save(new User(requestBody.getId(), requestBody.getName(), requestBody.getSurname(), false));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-info", method = RequestMethod.GET)
    public ResponseEntity<User> getUserInfo(@RequestParam Long userId) {
        Optional<User> opt = userRepository.findById(userId);
        return opt.isPresent() ? new ResponseEntity<>(opt.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/uzytkownik-modyfikuj", method = RequestMethod.PUT)
    public ResponseEntity modifyUser(@RequestBody UserRequestBody requestBody) {
        Optional<User> opt = userRepository.findById(requestBody.getId());
        if (opt.isPresent()) {
            User userToModify = opt.get();
            userToModify.setName(requestBody.getName());
            userToModify.setSurname(requestBody.getSurname());
            userRepository.save(userToModify);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/uzytkownik-lista", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-przyznaj-dostep-do-serwisu", method = RequestMethod.POST)
    public ResponseEntity grantAccessToService(@RequestBody AccessRequestBody requestBody ) {
        Optional<User> userOptional = userRepository.findById(requestBody.getUserId());
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (userOptional.isEmpty() || serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            Optional<Credential> credentialOptional = credentialRepository.findByServiceAndLogin(serviceOptional.get(), requestBody.getLogin());
            if (credentialOptional.isPresent()) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
            Credential credential = new Credential(
                    userOptional.get(),
                    serviceOptional.get(),
                    requestBody.getLogin(),
                    PasswordManagement.generateHash(requestBody.getPassword()));
            credentialRepository.save(credential);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/uzytkownik-odbierz-dostep-do-serwisu", method = RequestMethod.PUT)
    public ResponseEntity revokeAccessToService(@RequestBody RevokeAccessRequestBody requestBody) {
        Optional<User> userOptional = userRepository.findById(requestBody.getUserId());
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (userOptional.isEmpty() || serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            Optional<Credential> credentialOptional = credentialRepository.findByUserAndService(userOptional.get(), serviceOptional.get());
            if (credentialOptional.isEmpty()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                credentialRepository.delete(credentialOptional.get());
                return new ResponseEntity(HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "/uzytkownik-uprawnienia", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getUserPermissions(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(serviceRepository.getServicesByUser(userOptional.get()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/uzytkownik-zmiana-hasla", method = RequestMethod.PUT)
    public ResponseEntity changeUserPasswordForService(@RequestBody PasswordChangeRequestBody requestBody) {
        Optional<User> userOptional = userRepository.findById(requestBody.getUserId());
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (userOptional.isEmpty() || serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        Optional<Credential> credentialOptional = credentialRepository.findByUserAndService(userOptional.get(), serviceOptional.get());
        if (credentialOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Credential userCredential = credentialOptional.get();
        if (!PasswordManagement.check(requestBody.getCurrentPass(), userCredential.getHash())) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        userCredential.setHash(PasswordManagement.generateHash(requestBody.getNewPass()));
        credentialRepository.save(userCredential);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-zablokuj", method = RequestMethod.PUT)
    public ResponseEntity blockUser(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        User userToBlock = userOptional.get();
        userToBlock.setIsBlocked(true);
        userRepository.save(userToBlock);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uzytkownik-autentykacje ", method = RequestMethod.GET)
    public ResponseEntity<UserAuthenticationDetailsResponseBody> getUserAuthentications(@RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        UserAuthenticationDetailsResponseBody responseBody = new UserAuthenticationDetailsResponseBody();
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
