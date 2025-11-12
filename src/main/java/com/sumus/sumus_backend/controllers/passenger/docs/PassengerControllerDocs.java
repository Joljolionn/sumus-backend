package com.sumus.sumus_backend.controllers.passenger.docs;


import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Passenger", description = "Endpoints de funcionalidades básicas envolvendo usuários")
public interface PassengerControllerDocs {

    @Operation(summary = "Visualiza todos os usuários inseridos no banco", description = "Retorna uma lista com todos os usuários inseridos no sistema", responses = {
            @ApiResponse(responseCode = "200", description = "Retorna uma array com todos os usuários inseridos no banco (vazia se o banco estiver vazio)", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassengerDocument.class))))
    })
    @GetMapping(path = "/passenger/all")
    public ResponseEntity<List<PassengerDocument>> getAllPassengers();

    @GetMapping(path = "/passenger/{email}/photo")
    public ResponseEntity<byte[]> getPassengerPhoto(@AuthenticatednPrincipal UserDetails userDetails) throws IOException;

    @GetMapping(path = "/passenger/{email}/active")
    public ResponseEntity<Boolean> getPassengerActiveStatus(@AuthenticatednPrincipal UserDetails userDetails);

    @PostMapping(path = "/passenger/pcd/{email}/verifyConditions")
    public ResponseEntity<PassengerDocument> verifyPcdPassengerConditions(@AuthenticatednPrincipal UserDetails userDetails);
}
