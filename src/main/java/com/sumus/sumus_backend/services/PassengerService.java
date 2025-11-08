package com.sumus.sumus_backend.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistrationDto;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
import com.sumus.sumus_backend.domain.entities.PassengerDocument;

/**
 * Define o Contrato de Negócio (API) para a gestão de Passageiros.
 * * Esta interface representa a **Abstração** no padrão **Strategy**,
 * permitindo que a lógica de negócio real seja injetada e substituível,
 * aderindo ao Princípio da Inversão de Dependência (DIP).
 */
public interface PassengerService {

    // CORREÇÃO DE IMPORTS: Se o PassengerDocument não estiver importado, o Java não o encontra.
    PassengerDocument create(PassengerRegistrationDto userDto) throws IOException;

    List<PassengerDocument> listAll();

    // TODO: Implementar update de informações do passageiro
    Optional<PassengerDocument> update(PassengerRegistrationDto userDto) throws IOException;

    // TODO: Implementar delete de passageiro
    Boolean delete(String email);

    Optional<PassengerDocument> findByEmail(String email);

    AuthResult login(LoginRequest loginRequest);

    GridFsResource getPhotoResourceByPassengerEmail(String email);

    Boolean getActiveStatus(String email);

    PassengerDocument verifyPcdConditions(String email);

}
