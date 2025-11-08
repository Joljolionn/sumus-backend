package com.sumus.sumus_backend.controllers.driver.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.domain.entities.driver.DriverDocument;
import com.sumus.sumus_backend.services.driver.DriverService;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<DriverDocument>> getAllDrivers() {

        return new ResponseEntity<>(driverService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{email}/photo")
    public ResponseEntity<byte[]> getDriverPhoto(@PathVariable String email) throws IOException {
        GridFsResource photoResource = driverService.getPhotoResourceByDriverEmail(email);

        if (photoResource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = photoResource.getContentType();

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .body(photoResource.getContentAsByteArray());
    }

    @GetMapping(path = "/{email}")
    public ResponseEntity<DriverDocument> findDriverByEmail(@PathVariable String email) {
        Optional<DriverDocument> driverDocument = driverService.findByEmail(email);

        if (driverDocument.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(driverDocument.get(), HttpStatus.OK);

    }

    @PostMapping(path = "/{email}/verify")
    public ResponseEntity<DriverDocument> verifyDriver(@PathVariable String email){
        return ResponseEntity.ok().body(driverService.verifyDocuments(email));
    }

}
