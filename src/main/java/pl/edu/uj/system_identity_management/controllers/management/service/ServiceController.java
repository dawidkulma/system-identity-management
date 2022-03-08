package pl.edu.uj.system_identity_management.controllers.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.uj.system_identity_management.controllers.management.service.request_bodies.*;
import pl.edu.uj.system_identity_management.model.Service;
import pl.edu.uj.system_identity_management.repositories.ServiceRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @RequestMapping(value = "/serwis-dodaj", method = RequestMethod.POST)
    public ResponseEntity addService(@RequestBody ServiceRequestBody requestBody) {
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (serviceOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        serviceRepository.save(new Service(requestBody.getUrl()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/serwis-modyfikuj", method = RequestMethod.PUT)
    public ResponseEntity modifyService(@RequestBody ServiceModificationRequestBody requestBody) {
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getCurrentUrl());
        if (serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Service serviceToModify = serviceOptional.get();
        serviceToModify.setUrl(requestBody.getNewUrl());
        serviceRepository.save(serviceToModify);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/serwis-usun", method = RequestMethod.DELETE)
    public ResponseEntity deleteService(@RequestBody ServiceRequestBody requestBody) {
        Optional<Service> serviceOptional = serviceRepository.findByUrl(requestBody.getUrl());
        if (serviceOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Service serviceToDelete = serviceOptional.get();
        serviceRepository.delete(serviceToDelete);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/serwis-lista", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getServices() {
        return new ResponseEntity<>(serviceRepository.findAll(), HttpStatus.OK);
    }
}