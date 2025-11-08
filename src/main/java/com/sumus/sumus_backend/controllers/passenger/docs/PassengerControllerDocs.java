package com.sumus.sumus_backend.controllers.passenger.docs;


import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
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

@Tag(name = "Passengers", description = "Endpoints de funcionalidades básicas envolvendo usuários")
public interface PassengerControllerDocs {

    @Operation(summary = "Visualiza todos os usuários inseridos no banco", description = "Retorna uma lista com todos os usuários inseridos no sistema", responses = {
            @ApiResponse(responseCode = "200", description = "Retorna uma array com todos os usuários inseridos no banco (vazia se o banco estiver vazio)", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassengerDocument.class))))
    })
    @GetMapping(path = "/passengers/all")
    public ResponseEntity<List<PassengerDocument>> getAllPassengers();

    @GetMapping(path = "/passengers/{email}/photo")
    public ResponseEntity<byte[]> getPassengerPhoto(@PathVariable String email) throws IOException;

    @GetMapping(path = "/passengers/{email}/active")
    public ResponseEntity<Boolean> getPassengerActiveStatus(@PathVariable String email);

    @PostMapping(path = "/passengers/pcd/{email}/verifyConditions")
    public ResponseEntity<PassengerDocument> verifyPcdPassengerConditions(@PathVariable String email);
}
