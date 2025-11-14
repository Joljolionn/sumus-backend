package com.sumus.sumus_backend.services.passenger;

import java.io.IOException;
import java.util.Optional;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;
import com.sumus.sumus_backend.domain.dtos.response.PassengerListResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.PassengerResponseDto;
import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;

public interface PassengerService {

    PassengerResponseDto create(PassengerRegistration userDto) throws IOException;

    PassengerListResponseDto listAll();

    // TODO: Implementar update de informações do passageiro
    Optional<PassengerDocument> update(PassengerRegistration userDto) throws IOException;

    // TODO: Implementar delete de passageiro
    Boolean delete(String email);

    PassengerResponseDto findByEmail(String email);

    GridFsResource getPhotoResourceByPassengerEmail(String email);

    Boolean getActiveStatus(String email);

    PassengerResponseDto verifyPcdConditions(String email);

}
