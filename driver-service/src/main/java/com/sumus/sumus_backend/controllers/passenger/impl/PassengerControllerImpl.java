package com.sumus.sumus_backend.controllers.passenger.impl;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.controllers.passenger.docs.PassengerControllerDocs;
import com.sumus.sumus_backend.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.response.PassengerListResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.PassengerResponseDto;
import com.sumus.sumus_backend.services.passenger.PassengerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/passenger")
public class PassengerControllerImpl implements PassengerControllerDocs {

    @Autowired
    private PassengerService passengerService;

    @Override
    @GetMapping(path = "/all")
    public ResponseEntity<PassengerListResponseDto> getAllPassengers() {
        return new ResponseEntity<>(passengerService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/photo")
    public ResponseEntity<byte[]> getPassengerPhoto(@AuthenticationPrincipal UserDetails userDetails) throws IOException {

        GridFsResource photoResource = passengerService.getPhotoResourceByPassengerEmail(userDetails.getUsername());

        if (photoResource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = photoResource.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(photoResource.getContentAsByteArray());
    }

    @Override
    @PostMapping(path = "/pcd/verifyConditions")
    public ResponseEntity<PassengerResponseDto> verifyPcdPassengerConditions(@AuthenticationPrincipal UserDetails userDetails) {

        PassengerResponseDto passengerDocument = passengerService.verifyPcdConditions(userDetails.getUsername());

        if (passengerDocument == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(passengerDocument, HttpStatus.OK);
    }

    @GetMapping(path = "/")
    public ResponseEntity<PassengerResponseDto> getPassengerByEmail(@AuthenticationPrincipal UserDetails userDetails) {
        PassengerResponseDto passengerResponseDto = passengerService.findByEmail(userDetails.getUsername());

        if (passengerResponseDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(passengerResponseDto);
    }

	@Override
    @PutMapping(path = "/")
	public ResponseEntity<PassengerResponseDto> updatePassenger(UserDetails userDetails,
			@ModelAttribute @Valid PassengerUpdateRequest passengerUpdateRequest) throws IOException {

        try {
            PassengerResponseDto passengerResponseDto = passengerService.update(userDetails.getUsername(), passengerUpdateRequest);
            if (passengerResponseDto == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(passengerResponseDto);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        
	}

	@Override
    @PatchMapping(path = "/password")
	public ResponseEntity<Void> updatePassengerPassword(UserDetails userDetails,
	@RequestBody @Valid PasswordUpdateRequest passwordUpdateRequest) {
        Boolean updated = passengerService.updatePassword(userDetails.getUsername(), passwordUpdateRequest);

        if (!updated){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
	}

	@Override
    @DeleteMapping(path = "/")
	public ResponseEntity<Void> deletePassenger(UserDetails userDetails) {
        Boolean deleted = passengerService.delete(userDetails.getUsername());

        if (!deleted){
            return ResponseEntity.notFound().build();
        }
         return ResponseEntity.ok().build();
	}

}
