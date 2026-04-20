package com.sumus.passenger.controllers.docs;


import java.io.IOException;

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
import com.sumus.passenger.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.passenger.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.passenger.domain.dtos.response.PassengerListResponseDto;
import com.sumus.passenger.domain.dtos.response.PassengerResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Passenger", description = "Endpoints de funcionalidades básicas envolvendo usuários")
public interface PassengerControllerDocs {

  @Operation(summary = "Visualiza todos os usuários inseridos no banco",
      description = "Retorna uma lista com todos os usuários inseridos no sistema",
      responses = {@ApiResponse(responseCode = "200",
          description = "Retorna uma array com todos os usuários inseridos no banco (vazia se o banco estiver vazio)",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PassengerListResponseDto.class)))})
  @GetMapping(path = "/all")
  public ResponseEntity<PassengerListResponseDto> getAll();

  @GetMapping(path = "/photo")
  public ResponseEntity<byte[]> getPhoto(@AuthenticationPrincipal
  UserDetails userDetails) throws IOException;

  @PostMapping(path = "/pcd/verifyConditions")
  public ResponseEntity<PassengerResponseDto> postPcdConditions(@AuthenticationPrincipal
  UserDetails userDetails);

  @GetMapping(path = "/")
  public ResponseEntity<PassengerResponseDto> getByEmail(@AuthenticationPrincipal
  UserDetails userDetails);

  @PutMapping(path = "/")
  public ResponseEntity<PassengerResponseDto> putUpdate(UserDetails userDetails,
      @ModelAttribute
      @Valid
      PassengerUpdateRequest passengerUpdateRequest) throws IOException;

  @PatchMapping(path = "/password")
  public ResponseEntity<Void> patchPassword(UserDetails userDetails, @RequestBody
  @Valid
  PasswordUpdateRequest passwordUpdateRequest);

  @DeleteMapping(path = "/")
  public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal
  UserDetails userDetails);
}
