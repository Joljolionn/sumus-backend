package com.sumus.sumus_backend.services.passenger;

import java.io.IOException;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistrationRequest;
import com.sumus.sumus_backend.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.response.PassengerListResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.PassengerResponseDto;

public interface PassengerService {

    PassengerResponseDto create(PassengerRegistrationRequest userDto) throws IOException;

    PassengerListResponseDto listAll();

    PassengerResponseDto update(String email, PassengerUpdateRequest passengerUpdateRequest) throws IOException;

    Boolean delete(String email);

    PassengerResponseDto findByEmail(String email);

    GridFsResource getPhotoResourceByPassengerEmail(String email);

    Boolean getActiveStatus(String email);

    PassengerResponseDto verifyPcdConditions(String email);

    Boolean updatePassword(String email, PasswordUpdateRequest passwordUpdateRequest);

}
