package com.sumus.sumus_backend.controllers.docs;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.sumus.sumus_backend.domain.entities.PassengerDocument;

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

    // TODO: Adicionar documentação do endpoint para receber a foto do usuário

}
