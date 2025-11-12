package com.sumus.sumus_backend.services.passenger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;
import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;

public interface PassengerService {

    // CORREÇÃO DE IMPORTS: Se o PassengerDocument não estiver importado, o Java não
    // o encontra.
    PassengerDocument create(PassengerRegistration userDto) throws IOException;

    List<PassengerDocument> listAll();

    // TODO: Implementar update de informações do passageiro
    Optional<PassengerDocument> update(PassengerRegistration userDto) throws IOException;

    // TODO: Implementar delete de passageiro
    Boolean delete(String email);

    Optional<PassengerDocument> findByEmail(String email);

    GridFsResource getPhotoResourceByPassengerEmail(String email);

    Boolean getActiveStatus(String email);

    PassengerDocument verifyPcdConditions(String email);

}
