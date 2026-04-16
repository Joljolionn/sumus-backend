package com.sumus.passenger.services;

import java.io.IOException;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.passenger.domain.dtos.request.PassengerRegistrationRequest;
import com.sumus.passenger.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.passenger.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.passenger.domain.dtos.response.PassengerListResponseDto;
import com.sumus.passenger.domain.dtos.response.PassengerResponseDto;


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
