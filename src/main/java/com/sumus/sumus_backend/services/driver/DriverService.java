package com.sumus.sumus_backend.services.driver;

import java.io.IOException;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.DriverRegistration;
import com.sumus.sumus_backend.domain.dtos.request.DriverUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.response.DriverListResponseDTO;
import com.sumus.sumus_backend.domain.dtos.response.DriverResponseDto;

public interface DriverService {

    DriverResponseDto create(DriverRegistration driverRegistration) throws IOException;

    DriverListResponseDTO listAll();

    GridFsResource getPhotoResourceByDriverEmail(String email);

    DriverResponseDto findByEmail(String email);

    DriverResponseDto verifyDocuments(String email);

    DriverResponseDto update(String email, DriverUpdateRequest driverUpdateRequest) throws IOException;

    Boolean updatePassword(String email, PasswordUpdateRequest passwordUpdateRequest);

    Boolean deleteDriver(String email);

}
