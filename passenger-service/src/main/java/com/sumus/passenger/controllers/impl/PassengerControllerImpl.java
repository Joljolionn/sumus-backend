package com.sumus.passenger.controllers.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.passenger.controllers.docs.PassengerControllerDocs;
import com.sumus.passenger.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.passenger.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.passenger.domain.dtos.response.PassengerListResponseDto;
import com.sumus.passenger.domain.dtos.response.PassengerResponseDto;
import com.sumus.passenger.services.PassengerService;


@RestController
public class PassengerControllerImpl implements PassengerControllerDocs {

  @Autowired
  private PassengerService passengerService;

  @Override

  public ResponseEntity<PassengerListResponseDto> getAll() {
    return new ResponseEntity<>(passengerService.listAll(), HttpStatus.OK);
  }


  @Override
  public ResponseEntity<byte[]> getPhoto(UserDetails userDetails) throws IOException {

    GridFsResource photoResource =
        passengerService.getPhotoResourceByPassengerEmail(userDetails.getUsername());

    if (photoResource == null) {
      return ResponseEntity.notFound().build();
    }

    String contentType = photoResource.getContentType();

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .body(photoResource.getContentAsByteArray());
  }

  @Override
  public ResponseEntity<PassengerResponseDto> postPcdConditions(
      UserDetails userDetails) {

    PassengerResponseDto passengerDocument =
        passengerService.verifyPcdConditions(userDetails.getUsername());

    if (passengerDocument == null) {
      return ResponseEntity.notFound().build();
    }

    return new ResponseEntity<>(passengerDocument, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PassengerResponseDto> getByEmail(UserDetails userDetails) {
    PassengerResponseDto passengerResponseDto =
        passengerService.findByEmail(userDetails.getUsername());

    if (passengerResponseDto == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(passengerResponseDto);
  }

  @Override
  public ResponseEntity<PassengerResponseDto> putUpdate(UserDetails userDetails,
      PassengerUpdateRequest passengerUpdateRequest) throws IOException {

    try {
      PassengerResponseDto passengerResponseDto =
          passengerService.update(userDetails.getUsername(), passengerUpdateRequest);
      if (passengerResponseDto == null) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok(passengerResponseDto);

    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }

  }

  @Override
  public ResponseEntity<Void> patchPassword(UserDetails userDetails,
      PasswordUpdateRequest passwordUpdateRequest) {
    Boolean updated =
        passengerService.updatePassword(userDetails.getUsername(), passwordUpdateRequest);

    if (!updated) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> deleteAccount(UserDetails userDetails) {
    Boolean deleted = passengerService.delete(userDetails.getUsername());

    if (!deleted) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

}
