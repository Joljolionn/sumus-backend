package com.sumus.driver.services;

import java.io.IOException;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.driver.domain.dtos.request.DriverRegistrationRequest;
import com.sumus.driver.domain.dtos.request.DriverUpdateRequest;
import com.sumus.driver.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.driver.domain.dtos.response.DriverListResponseDto;
import com.sumus.driver.domain.dtos.response.DriverResponseDto;


public interface DriverService {

    DriverResponseDto create(DriverRegistrationRequest driverRegistration) throws IOException;

    DriverListResponseDto listAll();

    GridFsResource getPhotoResourceByDriverEmail(String email);

    DriverResponseDto findByEmail(String email);

    DriverResponseDto verifyDocuments(String email);

    DriverResponseDto update(String email, DriverUpdateRequest driverUpdateRequest) throws IOException;

    Boolean updatePassword(String email, PasswordUpdateRequest passwordUpdateRequest);

    Boolean deleteDriver(String email);

}
