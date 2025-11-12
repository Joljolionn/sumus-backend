package com.sumus.sumus_backend.controllers.passenger.impl;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.controllers.passenger.docs.PassengerControllerDocs;
import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;
import com.sumus.sumus_backend.services.passenger.PassengerService;

@RestController
@RequestMapping("/passenger")
public class PassengerControllerImpl implements PassengerControllerDocs {

    @Autowired
    private PassengerService userService;

    @Override
    @GetMapping(path = "/all")
    public ResponseEntity<List<PassengerDocument>> getAllPassengers() {
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/photo")
    public ResponseEntity<byte[]> getPassengerPhoto(@AuthenticationPrincipal UserDetails userDetails) throws IOException {

        GridFsResource photoResource = userService.getPhotoResourceByPassengerEmail(userDetails.getUsername());

        if (photoResource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = photoResource.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(photoResource.getContentAsByteArray());
    }

    @Override
    @GetMapping(path = "/active")
    public ResponseEntity<Boolean> getPassengerActiveStatus(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userService.getActiveStatus(userDetails.getUsername()), HttpStatus.OK);
    }

    @Override
    @PostMapping(path = "/pcd/verifyConditions")
    public ResponseEntity<PassengerDocument> verifyPcdPassengerConditions(@AuthenticationPrincipal UserDetails userDetails) {

        PassengerDocument passengerDocument = userService.verifyPcdConditions(userDetails.getUsername());

        if (passengerDocument == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(passengerDocument, HttpStatus.OK);
    }

}
