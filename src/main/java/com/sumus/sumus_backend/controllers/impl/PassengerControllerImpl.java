package com.sumus.sumus_backend.controllers.impl;

import java.io.IOException;
import java.util.List;

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

import com.sumus.sumus_backend.controllers.docs.PassengerControllerDocs;
import com.sumus.sumus_backend.domain.entities.PassengerDocument;
import com.sumus.sumus_backend.services.PassengerService;

@RestController
@RequestMapping("/passengers")
public class PassengerControllerImpl implements PassengerControllerDocs {

    @Autowired
    private PassengerService userService;

    @Override
    @GetMapping(path = "/all")
    public ResponseEntity<List<PassengerDocument>> getAllPassengers() {
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{email}/photo")
    public ResponseEntity<byte[]> getPassengerPhoto(@PathVariable String email) throws IOException {

        GridFsResource photoResource = userService.getPhotoResourceByPassengerEmail(email);

        if (photoResource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = photoResource.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(photoResource.getContentAsByteArray());
    }

    @Override
    @GetMapping(path = "/{email}/active")
    public ResponseEntity<Boolean> getPassengerActiveStatus(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPassengerActiveStatus'");
    }

    @Override
    @PostMapping(path = "/pcd/{email}/verifyConditions")
    public ResponseEntity<PassengerDocument> verifyPcdPassengerConditions(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyPcdPassengerConditions'");
    }

}
