package com.sumus.sumus_backend.controllers.passenger.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;
import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Passenger Auth", description = "Endpoints de funcionalidades básicas envolvendo autenticação")
public interface PassengerAuthControllerDocs {

    @Operation(summary = "Cadastra um novo usuário no sistema", description = "Recebe os dados iniciais de um usuário, o cadastra no sistema e retorna o usuário cadastrado no sistema", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerRegistration.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor (possivelmente ao processar a imagem do usuário", content = @Content)
    })
    @PostMapping(path = "/passenger/signup")
    public ResponseEntity<PassengerDocument> createPassenger(@ModelAttribute PassengerRegistration userDto);

    @Operation(summary = "Faz o login do usuário no sistema", description = "Recebe um email e uma senha e faz a busca no banco para confirmar se acha o email do usuário e se a senha está correta e retorna um 'token' de autenticação (por enquanto não implementado, retorna somente 'funcionou')", responses = {
            @ApiResponse(responseCode = "404", description = "Usuário com email enviado não foi encontrado no banco de dados", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário com email enviado foi encontrado porém a senha fornecida não está correta", content = @Content),
            @ApiResponse(responseCode = "200", description = "Login bem sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping(path = "/passenger/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest);
}
