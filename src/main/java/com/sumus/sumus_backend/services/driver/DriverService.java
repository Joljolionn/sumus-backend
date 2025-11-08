package com.sumus.sumus_backend.services.driver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.DriverRegistration;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
import com.sumus.sumus_backend.domain.entities.driver.DriverDocument;

public interface DriverService {

    DriverDocument create(DriverRegistration driverRegistration) throws IOException;

    List<DriverDocument> listAll();

    AuthResult login(LoginRequest loginRequest);

    GridFsResource getPhotoResourceByDriverEmail(String email);

    Optional<DriverDocument> findByEmail(String email);

    DriverDocument verifyDocuments(String email);
}
