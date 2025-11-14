package com.sumus.sumus_backend.controllers.passenger.docs;


import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sumus.sumus_backend.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.response.PassengerListResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.PassengerResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Passenger", description = "Endpoints de funcionalidades básicas envolvendo usuários")
public interface PassengerControllerDocs {

    @Operation(summary = "Visualiza todos os usuários inseridos no banco", description = "Retorna uma lista com todos os usuários inseridos no sistema", responses = {
            @ApiResponse(responseCode = "200", description = "Retorna uma array com todos os usuários inseridos no banco (vazia se o banco estiver vazio)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerListResponseDto.class)))
    })
    @GetMapping(path = "/passenger/all")
    public ResponseEntity<PassengerListResponseDto> getAllPassengers();

    @GetMapping(path = "/passenger/photo")
    public ResponseEntity<byte[]> getPassengerPhoto(@AuthenticationPrincipal UserDetails userDetails) throws IOException;

    @PostMapping(path = "/passenger/pcd/verifyConditions")
    public ResponseEntity<PassengerResponseDto> verifyPcdPassengerConditions(@AuthenticationPrincipal UserDetails userDetails);

    @PutMapping(path = "/passenger/")
    public ResponseEntity<PassengerResponseDto> updatePassenger(@AuthenticationPrincipal UserDetails userDetails, PassengerUpdateRequest passengerUpdateRequest) throws IOException;

    @PatchMapping(path = "/passenger/password")
    public ResponseEntity<Void> updatePassengerPassword(@AuthenticationPrincipal UserDetails userDetails, PasswordUpdateRequest passwordUpdateRequest);

    @DeleteMapping(path = "/passenger/")
    public ResponseEntity<Void> deletePassenger(@AuthenticationPrincipal UserDetails userDetails);
}
